package com.ky.inventoryservice.repository;

import com.ky.inventoryservice.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InventoryRepository extends JpaRepository<Inventory, UUID> {
    Long deleteByProductId(UUID productId);

    Inventory getByProductId(UUID productId);

    Inventory findByProductIdAndQuantityLessThan(UUID productId, Integer quantity);
}
