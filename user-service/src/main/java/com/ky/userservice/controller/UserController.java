package com.ky.userservice.controller;

import com.ky.userservice.dto.*;
import com.ky.userservice.exception.HttpResponse;
import com.ky.userservice.model.User;
import com.ky.userservice.model.UserPrincipal;
import com.ky.userservice.service.UserService;
import com.ky.userservice.util.AuthenticationHelper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import static com.ky.userservice.constant.FileConstant.*;
import static com.ky.userservice.constant.RequestConstant.*;
import static com.ky.userservice.constant.SecurityConstant.TOKEN_PREFIX;
import static org.apache.hc.core5.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;

@RestController
@RequestMapping("/v1/user")
public class UserController {
    private final UserService userService;
    private final AuthenticationHelper authenticationHelper;

    public UserController(UserService userService, AuthenticationHelper authenticationHelper) {
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterUserRequest user)  {
        userService.register(user);
        return ResponseEntity.ok(REGISTER_RES);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginUserRequest user) {
       authenticationHelper.authenticate(user.getEmail(), user.getPassword());
       User loginUser = userService.findUserByEmail(user.getEmail());
       UserPrincipal userPrincipal = new UserPrincipal(loginUser);
       LoginResponse loginResponse = authenticationHelper.getLoginResponse(userPrincipal);
       return ResponseEntity.ok(loginResponse);
    }

    @GetMapping("/token/refresh")
    public ResponseEntity<RefreshTokenResponse> refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException{

        String authorizationHeader = request.getHeader("refresh-token");
        return ResponseEntity.ok(authenticationHelper.validateRefreshToken(authorizationHeader, response));

    }

    @PostMapping("/validateToken")
    public ResponseEntity<UserDto> validateToken(@RequestParam String token) {
        return ResponseEntity.ok(userService.validateToken(token));
    }

    @GetMapping("/me")
    public ResponseEntity<MeDto> getMe(@RequestHeader(AUTHORIZATION) String authorizationHeader) {
        String token = authorizationHeader.substring(TOKEN_PREFIX.length());
        return ResponseEntity.ok(userService.getMe(token));
    }

    @GetMapping("/getById/{userId}")
    public ResponseEntity<UserCredential> getUserById(@PathVariable UUID userId) {
        return ResponseEntity.ok(userService.getUserCredentialsById(userId));
    }

    @PostMapping("/add")
    public ResponseEntity<String> addUser(@RequestBody AddUserRequest user)  {
        userService.addNewUser(user);
        return ResponseEntity.ok(ADD_USER_RES);
    }

    @PutMapping("/update")
    public ResponseEntity<LoginResponse> updateUser(@RequestBody UpdateUserRequest user)  {
        User currentUser = userService.updateUser(user);
        UserPrincipal userPrincipal = new UserPrincipal(currentUser);
        LoginResponse loginResponse = authenticationHelper.getLoginResponse(userPrincipal);
        return ResponseEntity.ok(loginResponse);
    }

    @PutMapping("/updatePassword")
    public ResponseEntity<LoginResponse> updatePassword(@RequestBody UpdatePasswordRequest user ,
                                                        @RequestHeader(AUTHORIZATION) String authorizationHeader)  {
        String token = authorizationHeader.substring(TOKEN_PREFIX.length());
        User currentUser = userService.updatePassword(user,token);
        UserPrincipal userPrincipal = new UserPrincipal(currentUser);
        LoginResponse loginResponse = authenticationHelper.getLoginResponse(userPrincipal);
        return ResponseEntity.ok(loginResponse);

    }

    @GetMapping("/find/{email}")
    public ResponseEntity<User> getUser(@PathVariable String email) {
        return ResponseEntity.ok(userService.findUserByEmail(email));
    }

    @GetMapping("/resetpassword/{email}")
    public ResponseEntity<HttpResponse> resetPassword(@PathVariable String email) {
        userService.resetPassword(email);
        return new ResponseEntity<>(new HttpResponse(OK.value(), OK, OK.getReasonPhrase().toUpperCase(),
                RESET_PASSWORD_RES + email), OK);
    }

    @DeleteMapping("/delete/{email}")
    public ResponseEntity<HttpResponse> deleteUser(@PathVariable String email){
        userService.deleteUser(email);
        return new ResponseEntity<>(new HttpResponse(OK.value(), OK, OK.getReasonPhrase().toUpperCase(),
                DELETE_USER_RES), OK);
    }

    @PostMapping("/updateProfileImage")
    public ResponseEntity<User> updateProfileImage(@RequestParam String email,
                                                   @RequestParam MultipartFile profileImage) {
        return ResponseEntity.ok(userService.updateProfileImage(email,profileImage));
    }

    @GetMapping(path = "/image/{email}/{fileName}", produces = IMAGE_JPEG_VALUE)
    public byte[] getProfileImage(@PathVariable String email, @PathVariable String fileName) throws IOException {
        return Files.readAllBytes(Paths.get(USER_FOLDER + email + FORWARD_SLASH + fileName));
    }

    @GetMapping(path = "/image/profile/{email}", produces = IMAGE_JPEG_VALUE)
    public byte[] getTempProfileImage(@PathVariable String email) throws IOException {
        URL url = new URL(TEMP_PROFILE_IMAGE_BASE_URL + email);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (InputStream inputStream = url.openStream()) {
            int bytesRead;
            byte[] chunk = new byte[1024];
            while((bytesRead = inputStream.read(chunk)) > 0) {
                byteArrayOutputStream.write(chunk, 0, bytesRead);
            }
        }
        return byteArrayOutputStream.toByteArray();
    }
}
