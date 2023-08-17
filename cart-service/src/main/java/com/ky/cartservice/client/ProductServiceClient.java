package com.ky.cartservice.client;

import com.ky.cartservice.dto.clientDto.ProductDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "product-service", path = "/v1/products")
public interface ProductServiceClient {
    Logger logger = LoggerFactory.getLogger(ProductServiceClient.class);

    @GetMapping("/{id}")
    ResponseEntity<ProductDto> getProductDtoById(@PathVariable UUID id);
}
