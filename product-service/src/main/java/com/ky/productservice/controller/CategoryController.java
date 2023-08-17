package com.ky.productservice.controller;

import com.ky.productservice.dto.category.CategoryDto;
import com.ky.productservice.dto.category.CreateCategoryRequest;
import com.ky.productservice.model.Category;
import com.ky.productservice.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id){
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAllCategories(){
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)

    public ResponseEntity<CategoryDto> createCategory(@RequestBody CreateCategoryRequest createCategoryRequest){
        return ResponseEntity.ok(categoryService.createCategory(createCategoryRequest));
    }


}
