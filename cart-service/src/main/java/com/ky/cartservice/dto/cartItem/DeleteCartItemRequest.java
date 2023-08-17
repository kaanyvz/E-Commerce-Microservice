package com.ky.cartservice.dto.cartItem;

import lombok.Getter;

import java.util.UUID;

@Getter
public class DeleteCartItemRequest {
    private UUID productId;
}
