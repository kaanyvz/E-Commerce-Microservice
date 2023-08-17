package com.ky.orderservice.controller;

import com.ky.orderservice.dto.Pagination;
import com.ky.orderservice.dto.order.CreateOrderRequest;
import com.ky.orderservice.dto.order.OrderDto;
import com.ky.orderservice.service.OrderService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/orders")
@Slf4j
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@Valid @RequestBody CreateOrderRequest createOrderRequest){
        log.info("Create order request was called");
        return new ResponseEntity<>(orderService.createOrder(createOrderRequest), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Pagination<OrderDto>> getAllOrders(@RequestParam(required = false, defaultValue = "0") int pageNo,
                                                             @RequestParam(required = false, defaultValue = "10") int pageSize){
        return  ResponseEntity.ok(orderService.getAllOrders(pageNo, pageSize));
    }
}
