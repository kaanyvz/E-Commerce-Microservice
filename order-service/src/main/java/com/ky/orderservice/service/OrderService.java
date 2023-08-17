package com.ky.orderservice.service;

import com.ky.orderservice.client.InventoryServiceClient;
import com.ky.orderservice.dto.Pagination;
import com.ky.orderservice.dto.inventory.InventoryCheckRequest;
import com.ky.orderservice.dto.inventory.InventoryCheckResponse;
import com.ky.orderservice.dto.order.CreateOrderRequest;
import com.ky.orderservice.dto.order.OrderDto;
import com.ky.orderservice.dto.order.OrderMapper;
import com.ky.orderservice.exception.ProductNotInStockException;
import com.ky.orderservice.model.Order;
import com.ky.orderservice.repository.OrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final InventoryServiceClient inventoryServiceClient;

    public OrderService(OrderRepository orderRepository, OrderMapper orderMapper, InventoryServiceClient inventoryServiceClient) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.inventoryServiceClient = inventoryServiceClient;
    }

    public OrderDto createOrder(CreateOrderRequest createOrderRequest){
        Order order = orderMapper.orderRequestToOrder(createOrderRequest);
        System.out.println(order);
        order.getAddress().setOrder(order);

        order.getItems().forEach(orderItem -> orderItem.setOrder(order));

        List<InventoryCheckRequest> inventoryCheckRequests = order.getItems().stream()
                .map(item -> new InventoryCheckRequest(item.getProductId(),item.getQuantity()))
                .collect(Collectors.toList());

        InventoryCheckResponse inventoryCheckResponse = inventoryServiceClient.isInStock(inventoryCheckRequests).getBody();

        if(!inventoryCheckResponse.getIsInStock()){
            throw new ProductNotInStockException(inventoryCheckResponse.getIsNotInStockProductIds().toString());
        }

        return orderMapper.orderToOrderDto(orderRepository.save(order));

    }

    public Pagination<OrderDto> getAllOrders(int pageNo, int pageSize){
        Pageable paging = PageRequest.of(pageNo, pageSize);
        Page<Order> orders = orderRepository.findAll(paging);

        return new Pagination<>(orders.stream().map(orderMapper::orderToOrderDto).collect(Collectors.toList()), orders.getTotalElements());
    }
}
