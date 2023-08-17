package com.ky.productservice.model;

import com.ky.common.model.BaseModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity(name = "categories")
@Table
@NoArgsConstructor
@AllArgsConstructor
@Data
@SuperBuilder
public class Category extends BaseModel {
    private String name;
    @OneToMany(mappedBy = "category")
    private List<Product> products;
}
