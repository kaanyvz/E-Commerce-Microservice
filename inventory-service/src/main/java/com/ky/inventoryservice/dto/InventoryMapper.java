package com.ky.inventoryservice.dto;

import com.ky.amqp.dto.InventoryRequest;
import com.ky.inventoryservice.client.ProductServiceClient;
import com.ky.inventoryservice.model.Inventory;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class InventoryMapper {
    public Inventory createInventoryRequestToInventory(InventoryRequest inventoryRequest){
        return Inventory.builder()
                .productId(inventoryRequest.getProductId())
                .quantity(inventoryRequest.getQuantity())
                .build();
    }
}
