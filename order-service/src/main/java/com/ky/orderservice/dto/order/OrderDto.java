package com.ky.orderservice.dto.order;


import com.ky.orderservice.dto.orderAddress.OrderAddressDto;
import com.ky.orderservice.dto.orderItem.OrderItemDto;
import com.ky.orderservice.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDto {
    private UUID id;
    private UUID customerId;
    private OrderAddressDto address;
    private List<OrderItemDto> items;
    private OrderStatus orderStatus;
    private LocalDateTime createdDate;
}
