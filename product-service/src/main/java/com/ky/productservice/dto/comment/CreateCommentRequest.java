package com.ky.productservice.dto.comment;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.UUID;

@Getter
public class CreateCommentRequest {

    @NotNull
    private UUID productId;

    @NotNull
    private String text;

    @NotNull
    private String creator;
}
