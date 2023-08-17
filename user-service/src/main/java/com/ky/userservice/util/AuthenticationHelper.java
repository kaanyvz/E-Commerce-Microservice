package com.ky.userservice.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ky.userservice.dto.LoginResponse;
import com.ky.userservice.dto.RefreshTokenResponse;
import com.ky.userservice.exception.TokenNotValidException;
import com.ky.userservice.model.User;
import com.ky.userservice.model.UserPrincipal;
import com.ky.userservice.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeTypeUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.ky.userservice.constant.SecurityConstant.Company_LLC;
import static com.ky.userservice.constant.SecurityConstant.TOKEN_PREFIX;

@Component
@RequiredArgsConstructor
public class AuthenticationHelper {

    @Value("${jwt.secret}")
    private String secret;

    private final JWTTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    public LoginResponse getLoginResponse(UserPrincipal user) {
        return new LoginResponse(jwtTokenProvider.generateAccessToken(user),jwtTokenProvider.generateRefreshToken(user)
                ,user.getRole());
    }

    public void authenticate(String email, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
    }

    @Transactional
    public RefreshTokenResponse validateRefreshToken(String authorizationHeader, HttpServletResponse response)
            throws IOException {
        if(authorizationHeader != null && authorizationHeader.startsWith(TOKEN_PREFIX)) {
            try {
                String refresh_token = authorizationHeader.substring(TOKEN_PREFIX.length());
                Algorithm algorithm = Algorithm.HMAC256(secret);
                JWTVerifier verifier = JWT.require(algorithm).withIssuer(Company_LLC).build();
                DecodedJWT decodedJWT = verifier.verify(refresh_token);
                String userId = decodedJWT.getSubject();
                User user = userService.getUserById(UUID.fromString(userId));
                UserPrincipal userPrincipal = new UserPrincipal(user);
                String accessToken = jwtTokenProvider.generateAccessToken(userPrincipal);
                return new RefreshTokenResponse(accessToken,refresh_token);

            }catch (Exception exception) {
                response.setHeader("error", exception.getMessage());
                response.setStatus(HttpStatus.FORBIDDEN.value());
                Map<String, String> error = new HashMap<>();
                error.put("error_message", exception.getMessage());
                response.setContentType(MimeTypeUtils.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        } else {
            throw new TokenNotValidException("Refresh token is missing");
        }
        return null;
    }

}
