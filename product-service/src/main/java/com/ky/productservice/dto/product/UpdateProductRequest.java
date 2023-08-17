package com.ky.productservice.dto.product;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class UpdateProductRequest {
    @NotNull
    private String name;
    @NotNull
    private BigDecimal unitPrice;
    @NotNull
    private String description;
    @NotNull
    private Long categoryId;
    @NotNull
    private String imageUrl;
}