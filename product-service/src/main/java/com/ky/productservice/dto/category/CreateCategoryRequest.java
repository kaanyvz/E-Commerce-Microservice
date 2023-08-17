package com.ky.productservice.dto.category;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CreateCategoryRequest {

    @NotNull
    private String name;
}
