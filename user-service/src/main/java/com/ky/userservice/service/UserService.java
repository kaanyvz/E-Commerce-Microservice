package com.ky.userservice.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.ky.amqp.RabbitMQMessageProducer;
import com.ky.amqp.dto.EmailRequest;
import com.ky.userservice.dto.*;
import com.ky.userservice.enumeration.Role;
import com.ky.userservice.exception.*;
import com.ky.userservice.model.User;
import com.ky.userservice.model.UserPrincipal;
import com.ky.userservice.repository.UserRepository;
import com.ky.userservice.util.JWTTokenProvider;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static com.ky.userservice.constant.FileConstant.*;
import static com.ky.userservice.constant.SecurityConstant.AUTHORITIES;
import static com.ky.userservice.constant.UserConstant.*;
import static com.ky.userservice.enumeration.Role.ROLE_USER;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.springframework.http.MediaType.*;

@Service
@RequiredArgsConstructor
@Transactional
@Qualifier("userDetailsService")
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final LoginAttemptService loginAttemptService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RabbitMQMessageProducer rabbitMQMessageProducer;
    private final JWTTokenProvider jwtTokenProvider;
    Logger logger = LoggerFactory.getLogger(UserService.class);


    @Override
    public UserDetails loadUserByUsername(String email) {
        User user = findUserByEmail(email);
        validateLoginAttempt(user);
        user.setLastLoginDateDisplay(user.getLastLoginDate());
        user.setLastLoginDate(new Date());
        userRepository.save(user);
        UserPrincipal userPrincipal = new UserPrincipal(user);
        logger.info(FOUND_USER_BY_EMAIL + email);
        return userPrincipal;
    }

    public User register(RegisterUserRequest request) {
        validateEmail(request.getEmail());
        User newUser = new User();
        newUser.setFirstName(request.getFirstName());
        newUser.setLastName(request.getLastName());
        newUser.setEmail(request.getEmail());
        newUser.setJoinDate(new Date());
        newUser.setPassword(encodePassword(request.getPassword()));
        newUser.setActive(true);
        newUser.setNotLocked(true);
        newUser.setRole(ROLE_USER.name());
        newUser.setAuthorities(ROLE_USER.getAuthorities());
        newUser.setProfileImageUrl(getTemporaryProfileImageUrl(request.getEmail()));
        userRepository.save(newUser);
        return newUser;

    }

    public User addNewUser(AddUserRequest request) {
        validateEmail(request.getEmail());
        User newUser = new User();
        newUser.setFirstName(request.getFirstName());
        newUser.setLastName(request.getLastName());
        newUser.setEmail(request.getEmail());
        newUser.setPassword(encodePassword(request.getPassword()));
        newUser.setJoinDate(new Date());
        newUser.setActive(request.isActive());
        newUser.setNotLocked(request.isNonLocked());
        newUser.setRole(getRoleEnumName(request.getRole()).name());
        newUser.setAuthorities(getRoleEnumName(request.getRole()).getAuthorities());
        newUser.setProfileImageUrl(getTemporaryProfileImageUrl(request.getEmail()));
        userRepository.save(newUser);
        return newUser;
    }

    public UserDto validateToken(String token) {
        DecodedJWT decodedJWT = jwtTokenProvider.decodeToken(token);
        String userId = decodedJWT.getClaim("userId").asString();
        String username = decodedJWT.getClaim("firstName").asString()
                + " "
                + decodedJWT.getClaim("lastName").asString();
        List<GrantedAuthority> authorities = jwtTokenProvider.getAuthorities(decodedJWT);
        return new UserDto(userId, authorities, username);
    }

    public MeDto getMe(String token) {
        DecodedJWT decodedJWT = jwtTokenProvider.decodeToken(token);

        List<String> roles = decodedJWT.getClaim(AUTHORITIES).asList(String.class);
        String userId = decodedJWT.getClaim("userId").asString();
        String firstName = decodedJWT.getClaim("firstName").asString();
        String lastName = decodedJWT.getClaim("lastName").asString();
        String email = decodedJWT.getClaim("email").asString();
        String profileImageUrl = decodedJWT.getClaim("profileImageURL").asString();

        return MeDto.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .userId(userId)
                .profileImageUrl(profileImageUrl)
                .roles(roles)
                .build();
    }

    public User updateUser(UpdateUserRequest user) {
        User currentUser = findUserByEmail(user.getEmail());
        currentUser.setFirstName(user.getFirstName());
        currentUser.setLastName(user.getLastName());
        currentUser.setProfileImageUrl(user.getProfileImageURL());
        return userRepository.save(currentUser);
    }

    public User updatePassword(UpdatePasswordRequest request, String token) {
        DecodedJWT decodedJWT = jwtTokenProvider.decodeToken(token);
        String email = decodedJWT.getClaim("email").asString();
        User currentUser = findUserByEmail(email);

        if (passwordEncoder.matches(request.getCurrentPassword(), currentUser.getPassword())) {
            currentUser.setPassword(encodePassword(request.getNewPassword()));
        } else {
            throw new PasswordNotMatchException("The current password is not correct!");
        }
        return userRepository.save(currentUser);
    }

    public void deleteUser(String email) {
        User user = findUserByEmail(email);
        Path userFolder = Paths.get(USER_FOLDER + user.getEmail()).toAbsolutePath().normalize();

        try {
            FileUtils.deleteDirectory(new File(userFolder.toString()));
        } catch (IOException e) {
            throw new FileDeleteException(FILE_DELETE_ERROR);
        }
        userRepository.deleteById(user.getId());
    }

    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email).orElseThrow(() -> new EmailNotFoundException(NO_USER_FOUND_BY_EMAIL));
    }
        public User updateProfileImage(String email, MultipartFile profileImage) {
        User user = findUserByEmail(email);
        saveProfileImage(user, profileImage);
        return user;
    }

    public void resetPassword(String email){
        User user = findUserByEmail(email);
        if (user == null) {
            throw new EmailNotFoundException(NO_USER_FOUND_BY_EMAIL + email);
        }
        String password = generatePassword();
        user.setPassword(encodePassword(password));
        userRepository.save(user);
        logger.info("New user password: " + password);
        sendEmail(user, password);
    }


//    PRIVATE METHODS

    private void sendEmail(User user, String password) {
        String text = "Hello " + user.getFirstName() + ", \n \n Your new account password is: " + password + "\n \n The Support Team";
        String subject = "KY - New Password";
        EmailRequest emailRequest = new EmailRequest(text, user.getEmail(),subject);
        rabbitMQMessageProducer.publish(
                emailRequest,
                "notification.exchange",
                "send.email.routing-key"
        );
    }


    private void validateEmail(String email) {
        Optional<User> userByNewEmail = userRepository.findUserByEmail(email);
        if (userByNewEmail.isPresent()) {
            throw new EmailExistException(EMAIL_ALREADY_EXISTS);
        }
    }

    private void validateLoginAttempt(User user) {
        if (user.isNotLocked()) {
            if (loginAttemptService.hasExceededMaxAttempts(user.getEmail())) {
                user.setNotLocked(false);
            } else {
                user.setNotLocked(true);
            }
        } else {
            loginAttemptService.evictUserFromLoginAttemptCache(user.getEmail());
        }
    }

    private void saveProfileImage(User user, MultipartFile profileImage) {
        if (Arrays.asList(IMAGE_JPEG_VALUE, IMAGE_PNG_VALUE, IMAGE_GIF_VALUE).contains(profileImage.getContentType())) {
            throw new NotAnImageFileException(profileImage.getOriginalFilename() + NOT_AN_IMAGE_FILE);
        }
        try {
            Path userFolder = Paths.get(USER_FOLDER + user.getEmail()).toAbsolutePath().normalize();
            if (!Files.exists(userFolder)) {
                Files.createDirectories(userFolder);
                logger.info(DIRECTORY_CREATED + userFolder);
            }
            Files.deleteIfExists(Paths.get(userFolder + user.getEmail() + DOT + JPG_EXTENSION));
            Files.copy(profileImage.getInputStream(), userFolder.resolve(user.getEmail() + DOT + JPG_EXTENSION), REPLACE_EXISTING);
            user.setProfileImageUrl(setProfileImageUrl(user.getEmail()));
            userRepository.save(user);
            logger.info(FILE_SAVED_IN_FILE_SYSTEM + profileImage.getOriginalFilename());
        } catch (IOException e) {
            throw new FileUploadException(FILE_UPLOAD_ERROR);
        }

    }

    private String setProfileImageUrl(String username) {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path(USER_IMAGE_PATH + username + FORWARD_SLASH
                + username + DOT + JPG_EXTENSION).toUriString();
    }

    private String getTemporaryProfileImageUrl(String email) {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path(DEFAULT_USER_IMAGE_PATH + email).toUriString();
    }

    private Role getRoleEnumName(String role) {
        return Role.valueOf(role.toUpperCase());
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    private String generatePassword() {
        return RandomStringUtils.randomAlphanumeric(10);
    }


    public User getUserById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User could not found by id " + id));
    }

    public UserCredential getUserCredentialsById(UUID id) {
        User user = userRepository.getById(id);
        return new UserCredential(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail());
    }
}





























