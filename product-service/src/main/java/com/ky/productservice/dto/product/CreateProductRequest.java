package com.ky.productservice.dto.product;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class CreateProductRequest {

    @NotNull
    private String name;

    @NotNull
    private BigDecimal price;

    @NotNull
    private Long categoryId;

    @NotNull
    private String description;

    @NotNull
    private Integer quantityInStock;

    @NotNull
    private String imageUrl;
}
