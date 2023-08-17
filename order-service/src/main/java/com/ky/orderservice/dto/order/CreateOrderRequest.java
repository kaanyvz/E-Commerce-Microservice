package com.ky.orderservice.dto.order;

import com.ky.orderservice.dto.orderAddress.CreateOrderAddressRequest;
import com.ky.orderservice.dto.orderItem.CreateOrderItemRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

@Getter
public class CreateOrderRequest {
    @NotNull
    private CreateOrderAddressRequest address;
    @NotNull
    private List<CreateOrderItemRequest> items;
}
