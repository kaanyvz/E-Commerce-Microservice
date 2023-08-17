package com.ky.userservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class RegisterUserRequest {
    @NotNull
    private String email;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    private String password;
}
