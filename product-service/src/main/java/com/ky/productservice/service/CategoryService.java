package com.ky.productservice.service;

import com.ky.productservice.dto.category.CategoryDto;
import com.ky.productservice.dto.category.CategoryMapper;
import com.ky.productservice.dto.category.CreateCategoryRequest;
import com.ky.productservice.exception.CategoryNotFoundException;
import com.ky.productservice.model.Category;
import com.ky.productservice.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    public Category getCategoryById(Long id){
        return categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category could not found by id: " + id));
    }

    public CategoryDto createCategory(CreateCategoryRequest createCategoryRequest){
         Category category = Category.builder()
                .name(createCategoryRequest.getName())
                .build();

         Category savedCategory = categoryRepository.save(category);

         return categoryMapper.categoryToCategoryDto(savedCategory);
    }

    public List<CategoryDto> getAllCategories(){
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::categoryToCategoryDto)
                .collect(Collectors.toList());
    }
}
