package com.ky.orderservice.dto.order;

import com.ky.orderservice.dto.orderAddress.OrderAddressMapper;
import com.ky.orderservice.dto.orderItem.OrderItemMapper;
import com.ky.orderservice.model.Order;
import com.ky.orderservice.model.OrderStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Component
public class OrderMapper {

    private final OrderAddressMapper orderAddressMapper;
    private final OrderItemMapper orderItemMapper;

    public OrderMapper(OrderAddressMapper orderAddressMapper, OrderItemMapper orderItemMapper) {
        this.orderAddressMapper = orderAddressMapper;
        this.orderItemMapper = orderItemMapper;
    }

    public OrderDto orderToOrderDto(Order order){
        return OrderDto.builder()
                .id(order.getId())
                .customerId(order.getCustomerId())
                .address(orderAddressMapper.orderAddressToOrderAddressDto(order.getAddress()))
                .items(order.getItems().stream().map(orderItemMapper::orderItemToOrderItemDto).collect(Collectors.toList()))
                .orderStatus(order.getOrderStatus())
                .createdDate(order.getCreatedDate())
                .build();
    }

    public Order orderRequestToOrder(CreateOrderRequest createOrderRequest){
        return Order.builder()
                .orderStatus(OrderStatus.PENDING)
                .createdDate(LocalDateTime.now())
                .address(orderAddressMapper.orderAddressRequestToOrderAddress(createOrderRequest.getAddress()))
                .items(createOrderRequest.getItems()
                        .stream()
                        .map(orderItemMapper::orderItemRequestToOrderItem)
                        .collect(Collectors.toList()))
                .build();
    }
}
