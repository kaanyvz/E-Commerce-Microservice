package com.ky.orderservice.client;

import com.ky.orderservice.dto.inventory.InventoryCheckRequest;
import com.ky.orderservice.dto.inventory.InventoryCheckResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient(
        name = "inventory-service",
        path = "/v1/inventories"
)
public interface InventoryServiceClient {

    @PostMapping("/isInStock")
    public ResponseEntity<InventoryCheckResponse> isInStock(List<InventoryCheckRequest> inventoryCheckRequests);
}
