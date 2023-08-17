package com.ky.userservice.dto;

import lombok.Getter;

@Getter
public class UpdatePasswordRequest {
    private String currentPassword;
    private String newPassword;
}
