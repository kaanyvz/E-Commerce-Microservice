package com.ky.cartservice.dto.cartItem;

import com.ky.cartservice.client.ProductServiceClient;
import com.ky.cartservice.model.CartItem;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class CartItemMapper {
    private final ProductServiceClient productServiceClient;

    public CartItemMapper(ProductServiceClient productServiceClient) {
        this.productServiceClient = productServiceClient;
    }

    public CartItem createCartItemRequestToCartItem(CreateCartItemRequest createCartItemRequest){
        return CartItem.builder()
                .name(Objects.requireNonNull(productServiceClient.getProductDtoById(createCartItemRequest.getProductId()).getBody()).getName())
                .price(Objects.requireNonNull(productServiceClient.getProductDtoById(createCartItemRequest.getProductId()).getBody()).getUnitPrice())
                .productId(Objects.requireNonNull(productServiceClient.getProductDtoById(createCartItemRequest.getProductId()).getBody()).getId())
                .quantity(createCartItemRequest.getQuantity())
                .build();
    }
}
