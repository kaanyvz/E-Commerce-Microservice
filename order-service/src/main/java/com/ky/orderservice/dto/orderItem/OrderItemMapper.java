package com.ky.orderservice.dto.orderItem;

import com.ky.orderservice.model.OrderItem;
import org.springframework.stereotype.Component;

@Component
public class OrderItemMapper {

    public OrderItemDto orderItemToOrderItemDto(OrderItem orderItem){
        return OrderItemDto.builder()
                .productId(orderItem.getProductId())
                .quantity(orderItem.getQuantity())
                .build();
    }

    public OrderItem orderItemRequestToOrderItem(CreateOrderItemRequest createOrderItemRequest){
        return OrderItem.builder()
                .productId(createOrderItemRequest.getProductId())
                .quantity(createOrderItemRequest.getQuantity())
                .build();
    }

}
