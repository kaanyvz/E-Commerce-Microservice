package com.ky.cartservice.dto.cartItem;

import lombok.Getter;

import java.util.UUID;

@Getter
public class UpdateCartItemRequest {
    private UUID productId;
    private Integer quantity;
}
