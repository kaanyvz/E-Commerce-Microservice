package com.ky.cartservice.dto.cart;

import com.ky.cartservice.model.CartItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartDto {
    private UUID customerId;
    private List<CartItem> cartItems;
    private BigDecimal totalPrice;
}
