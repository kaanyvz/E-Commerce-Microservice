package com.ky.orderservice.dto.orderItem;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderItemRequest {

    @NotNull
    private UUID productId;

    @NotNull
    private Integer quantity;
}
