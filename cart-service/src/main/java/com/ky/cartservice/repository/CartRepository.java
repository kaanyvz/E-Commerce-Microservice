package com.ky.cartservice.repository;

import com.ky.cartservice.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CartRepository extends JpaRepository<Cart, String> {
    Optional<Cart> findCartByCustomerId(UUID customerId);
}
