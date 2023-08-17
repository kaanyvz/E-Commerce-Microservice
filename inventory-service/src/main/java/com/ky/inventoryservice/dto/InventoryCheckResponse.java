package com.ky.inventoryservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
@ToString
public class InventoryCheckResponse {

    List<UUID> isNotInStockProductIds;
    Boolean isInStock;
}
