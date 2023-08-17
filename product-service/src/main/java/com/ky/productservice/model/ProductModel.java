package com.ky.productservice.model;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductModel implements Serializable {
    @Id
    private UUID id;
    private String name;
    private BigDecimal unitPrice;
    private String description;
    private String categoryName;
    private LocalDate createdDate;
    private String imageUrl;
}

























