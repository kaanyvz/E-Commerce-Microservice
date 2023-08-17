package com.ky.inventoryservice.service;

import com.ky.amqp.dto.DeleteInventoryRequest;
import com.ky.amqp.dto.InventoryRequest;
import com.ky.inventoryservice.dto.*;
import com.ky.inventoryservice.model.Inventory;
import com.ky.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {
    private final InventoryRepository inventoryRepository;
    private final InventoryMapper inventoryMapper;
    public void addProductToInventory(InventoryRequest inventoryRequest){
        inventoryRepository.save(inventoryMapper.createInventoryRequestToInventory(inventoryRequest));
    }

    @Transactional
    public void deleteProductFromInventory(DeleteInventoryRequest deleteInventoryRequest){
        inventoryRepository.deleteByProductId(deleteInventoryRequest.getProductId());
    }

    public void updateProductFromInventory(InventoryRequest inventoryRequest){
        Inventory inventory = inventoryRepository.getByProductId(inventoryRequest.getProductId());
        inventory.setQuantity(inventoryRequest.getQuantity());
        inventoryRepository.save(inventory);
    }

    public InventoryCheckResponse isInStock(List<InventoryCheckRequest> inventoryCheckRequests) {
        List<Inventory> inventories = inventoryCheckRequests.stream().map(inventoryRequest -> inventoryRepository
                        .findByProductIdAndQuantityLessThan(inventoryRequest.getProductId(), inventoryRequest.getQuantity()))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        List<UUID> inventorIds = inventories.stream()
                .map(Inventory::getProductId).collect(Collectors.toList());


        return InventoryCheckResponse.builder()
                .isNotInStockProductIds(inventorIds)
                .isInStock(inventorIds.size() == 0)
                .build();
    }
}

























