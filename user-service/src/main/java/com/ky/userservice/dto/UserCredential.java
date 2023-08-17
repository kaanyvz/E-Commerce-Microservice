package com.ky.userservice.dto;

import lombok.*;

import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class UserCredential {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
}
