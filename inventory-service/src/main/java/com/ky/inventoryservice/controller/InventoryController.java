package com.ky.inventoryservice.controller;

import com.ky.amqp.dto.InventoryRequest;
import com.ky.inventoryservice.dto.InventoryCheckRequest;
import com.ky.inventoryservice.dto.InventoryCheckResponse;
import com.ky.inventoryservice.service.InventoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/inventories")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @PutMapping
    public void updateInventory(InventoryRequest inventoryRequest){
        inventoryService.updateProductFromInventory(inventoryRequest);
    }

    @PostMapping
    public void createInventory(InventoryRequest inventoryRequest){
        inventoryService.addProductToInventory(inventoryRequest);
    }

    @PostMapping("/isInStock")
    public ResponseEntity<InventoryCheckResponse> isInStock(@RequestBody List<InventoryCheckRequest> inventoryCheckRequests){
        return ResponseEntity.ok(inventoryService.isInStock(inventoryCheckRequests));
    }

}
