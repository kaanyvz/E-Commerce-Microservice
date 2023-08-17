package com.ky.cartservice.dto.cartItem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemDto {

    private Long id;
    private UUID productId;
    private String name;
    private BigDecimal price;
    private BigDecimal totalPrice;
    private Integer quantity;
}
