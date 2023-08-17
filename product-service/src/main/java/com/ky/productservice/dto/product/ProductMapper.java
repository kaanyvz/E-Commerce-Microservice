package com.ky.productservice.dto.product;

import com.ky.productservice.dto.category.CategoryDto;
import com.ky.productservice.dto.category.CategoryMapper;
import com.ky.productservice.dto.comment.CommentMapper;
import com.ky.productservice.model.Category;
import com.ky.productservice.model.Product;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ProductMapper {
    private final CommentMapper commentMapper;
    private final CategoryMapper categoryMapper;

    public ProductMapper(CommentMapper commentMapper, CategoryMapper categoryMapper) {
        this.commentMapper = commentMapper;
        this.categoryMapper = categoryMapper;
    }

    public ProductDto productToProductDto(Product product){
        return ProductDto.builder()
                .name(product.getName())
                .id(product.getId())
                .unitPrice(product.getUnitPrice())
                .description(product.getDescription())
                .category(categoryMapper.categoryToCategoryDto(product.getCategory()))
                .createdDate(product.getCreatedDate())
                .imageUrl(product.getImageUrl())
                .comments(product.getComments().stream().map(commentMapper::commentToCommentDto).collect(Collectors.toList()))
                .build();
    }

}
