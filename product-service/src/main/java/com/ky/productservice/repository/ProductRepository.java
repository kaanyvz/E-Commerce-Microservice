package com.ky.productservice.repository;

import com.ky.productservice.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    List<Product> findByIdIn(List<UUID> productIds);
}
