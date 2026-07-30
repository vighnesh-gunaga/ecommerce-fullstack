package com.example.EcommarceWebsite.service;

import com.example.EcommarceWebsite.dto.CategoryRequest;
import com.example.EcommarceWebsite.dto.CategoryResponse;
import com.example.EcommarceWebsite.exception.CategoryNotFoundException;
import com.example.EcommarceWebsite.model.Category;
import com.example.EcommarceWebsite.repository.CategoryRepository;
import com.example.EcommarceWebsite.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    public List<CategoryResponse> getAllCategory() {
        List<Category> categories = categoryRepository.findAll();

        return categories.stream().map(category -> {
            CategoryResponse categoryResponse = new CategoryResponse();
            categoryResponse.setId(category.getId());
            categoryResponse.setName(category.getName());
            categoryResponse.setDescription(category.getDescription());
            categoryResponse.setCreatedAt(category.getCreatedAt());
            categoryResponse.setUpdatedAt(category.getUpdatedAt());
            return categoryResponse;
        }).toList();
    }

    public CategoryResponse getCategoryById(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(()->new
                CategoryNotFoundException("Not found"));
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setId(category.getId());
        categoryResponse.setName(category.getName());
        categoryResponse.setDescription(category.getDescription());
        categoryResponse.setCreatedAt(category.getCreatedAt());
        categoryResponse.setUpdatedAt(category.getUpdatedAt());
        return categoryResponse;
    }

    public CategoryResponse getCategoryByName(String name) {

        Category category = categoryRepository.findByName(name)
                .orElseThrow(() ->
                        new CategoryNotFoundException(
                                "Category with name " + name + " not found"
                        )
                );
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setId(category.getId());
        categoryResponse.setName(category.getName());
        categoryResponse.setDescription(category.getDescription());
        categoryResponse.setCreatedAt(category.getCreatedAt());
        categoryResponse.setUpdatedAt(category.getUpdatedAt());
        return categoryResponse;
    }
    public CategoryResponse addcategory(CategoryRequest categoryRequest) {
        Category category = new Category();

        category.setName(categoryRequest.getName());
        category.setDescription(categoryRequest.getDescription());

        Category savedCategory = categoryRepository.save(category);

        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setId(savedCategory.getId());
        categoryResponse.setName(savedCategory.getName());
        categoryResponse.setDescription(savedCategory.getDescription());
        categoryResponse.setCreatedAt(savedCategory.getCreatedAt());
        categoryResponse.setUpdatedAt(savedCategory.getUpdatedAt());
        return categoryResponse;

    }

    public CategoryResponse updateCategory(Category category, Long id) {

        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new CategoryNotFoundException(
                                "Category with id " + id + " not found"
                        )
                );

        existingCategory.setName(category.getName());
        existingCategory.setDescription(category.getDescription());

        Category updateCategory = categoryRepository.save(existingCategory);
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setId(updateCategory.getId());
        categoryResponse.setName(updateCategory.getName());
        categoryResponse.setDescription(updateCategory.getDescription());
        categoryResponse.setCreatedAt(updateCategory.getCreatedAt());
        categoryResponse.setUpdatedAt(updateCategory.getUpdatedAt());
        return categoryResponse;
    }


    public void deleteCategory(Long id) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new CategoryNotFoundException("Category not found"));

        if (productRepository.existsByCategoryId(id)) {
            throw new IllegalStateException(
                    "Cannot delete category because products exist in this category"
            );
        }

        categoryRepository.delete(category);
    }
}
