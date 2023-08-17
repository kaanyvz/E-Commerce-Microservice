package com.ky.productservice.service;

import com.ky.amqp.RabbitMQMessageProducer;
import com.ky.amqp.dto.DeleteInventoryRequest;
import com.ky.amqp.dto.InventoryRequest;
import com.ky.productservice.dto.Pagination;
import com.ky.productservice.dto.comment.CommentDto;
import com.ky.productservice.dto.comment.CommentMapper;
import com.ky.productservice.dto.product.CreateProductRequest;
import com.ky.productservice.dto.product.ProductDto;
import com.ky.productservice.dto.product.ProductMapper;
import com.ky.productservice.dto.product.UpdateProductRequest;
import com.ky.productservice.exception.ProductNotFoundException;
import com.ky.productservice.model.Category;
import com.ky.productservice.model.Product;
import com.ky.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CommentMapper commentMapper;
    private final CategoryService categoryService;
    private final RabbitMQMessageProducer rabbitMQMessageProducer;


    public Pagination<ProductDto> getAllProducts(int pageNo, int pageSize) {
        Pageable paging = PageRequest.of(pageNo, pageSize);
        Page<Product> products = productRepository.findAll(paging);

        return new Pagination<>(products.stream().map(productMapper::productToProductDto).collect(Collectors.toList()),
                products.getTotalElements());
    }

    public ProductDto getProductDtoById(UUID id){
        return productRepository.findById(id)
                .map(productMapper::productToProductDto)
                .orElseThrow(() -> new ProductNotFoundException("Product could not found by id: " + id));
    }

    public Product getProductById(UUID id){
        return productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Product could not found by id: " + id));
    }

    public List<ProductDto> getProductsByIds(List<UUID> productIds) {
        List<Product> products = productRepository.findByIdIn(productIds);
        return products.stream().map(productMapper::productToProductDto).collect(Collectors.toList());
    }

    public List<CommentDto> getCommentsByProductId(UUID id){
        Product product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Product could not found by id: " + id));

        return product.getComments().stream().map(commentMapper::commentToCommentDto).collect(Collectors.toList());

    }

    @Transactional
    public ProductDto createProduct(CreateProductRequest createProductRequest){

        Category category = categoryService.getCategoryById(createProductRequest.getCategoryId());

        Product product =  Product.builder()
                .name(createProductRequest.getName())
                .unitPrice(createProductRequest.getPrice())
                .description(createProductRequest.getDescription())
                .category(category)
                .imageUrl(createProductRequest.getImageUrl())
                .createdDate(LocalDateTime.now())
                .comments(new ArrayList<>())
                .build();

        Product savedProduct = productRepository.save(product);

        InventoryRequest inventoryRequest = new InventoryRequest(savedProduct.getId(),
                createProductRequest.getQuantityInStock());
        rabbitMQMessageProducer.publish(
                inventoryRequest,
                "inventory.exchange",
                "create.inventory.routing-key"
        );


        return productMapper.productToProductDto(savedProduct);
    }

    public ProductDto updateProduct(UpdateProductRequest updateProductRequest, UUID id){
        Product product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Product could not found by id: " + id));
        Category category = categoryService.getCategoryById(updateProductRequest.getCategoryId());

        product.setCategory(category);
        product.setName(updateProductRequest.getName());
        product.setUnitPrice(updateProductRequest.getUnitPrice());
        product.setDescription(updateProductRequest.getDescription());
        product.setImageUrl(updateProductRequest.getImageUrl());

        Product savedProduct = productRepository.save(product);

        return productMapper.productToProductDto(savedProduct);
    }

    @Transactional
    public UUID deleteProductById(UUID id){
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product could not found by id: " + id));
        productRepository.delete(product);
        log.info("Product " + " has deleted from repository.");

        DeleteInventoryRequest deleteInventoryRequest = new DeleteInventoryRequest(id);
        rabbitMQMessageProducer.publish(
                deleteInventoryRequest,
                "inventory.exchange",
                "delete.inventory.routing-key"
        );
        return id;
    }
}

