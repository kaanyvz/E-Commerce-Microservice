package com.ky.productservice.controller;

import com.ky.productservice.dto.Pagination;
import com.ky.productservice.dto.comment.CommentDto;
import com.ky.productservice.dto.product.CreateProductRequest;
import com.ky.productservice.dto.product.ProductDto;
import com.ky.productservice.dto.product.UpdateProductRequest;
import com.ky.productservice.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<Pagination<ProductDto>> getAllProducts(){
        return ResponseEntity.ok(productService.getAllProducts(1,2));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductDtoById(@PathVariable UUID id){
        return ResponseEntity.ok(productService.getProductDtoById(id));
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<List<CommentDto>> getCommentsByProductId(@PathVariable UUID id){
        return ResponseEntity.ok(productService.getCommentsByProductId(id));
    }

    @GetMapping("/findByIds/{productIds}")
    public ResponseEntity<List<ProductDto>> getProductsByIds(@PathVariable List<UUID> productIds){
        return ResponseEntity.ok(productService.getProductsByIds(productIds));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<ProductDto> createProduct(@RequestBody CreateProductRequest createProductRequest){
        return ResponseEntity.ok(productService.createProduct(createProductRequest));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<ProductDto> updateProduct(@RequestBody UpdateProductRequest updateProductRequest,
                                                    @PathVariable UUID id){
        return ResponseEntity.ok(productService.updateProduct(updateProductRequest, id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<UUID> deleteProduct(@PathVariable UUID id){
        return ResponseEntity.ok(productService.deleteProductById(id));
    }
}
