package com.ky.inventoryservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class InventoryCheckRequest {
    UUID productId;
    Integer quantity;

    public static class DeleteInventoryRequest {
        private UUID productId;

        public DeleteInventoryRequest(UUID productId) {
            this.productId = productId;
        }

        public DeleteInventoryRequest(){

        }

        public UUID getProductId() {
            return productId;
        }

        public void setProductId(UUID productId) {
            this.productId = productId;
        }

    }
}
