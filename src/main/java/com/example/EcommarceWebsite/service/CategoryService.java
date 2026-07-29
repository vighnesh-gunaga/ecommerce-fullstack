package com.example.EcommarceWebsite.service;

import com.example.EcommarceWebsite.dto.CategoryRequest;
import com.example.EcommarceWebsite.dto.CategoryResponse;
import com.example.EcommarceWebsite.exception.CategoryNotFoundException;
import com.example.EcommarceWebsite.model.Category;
import com.example.EcommarceWebsite.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getAllCategory() {
        return categoryRepository.findAll();
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(()->new
                CategoryNotFoundException("Not found"));
    }

//    public Category getCategoryByName(String name) {
//        return categoryRepository.findByName(name).orElseThrow(()->new CategoryNotFoundException("Category with name "+name+" Not found"));
//    }
    public Category getCategoryByName(String name) {

        return categoryRepository.findByName(name)
                .orElseThrow(() ->
                        new CategoryNotFoundException(
                                "Category with name " + name + " not found"
                        )
                );
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

    public Category updateCategory(Category category, Long id) {

        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new CategoryNotFoundException(
                                "Category with id " + id + " not found"
                        )
                );

        existingCategory.setName(category.getName());
        existingCategory.setDescription(category.getDescription());

        return categoryRepository.save(existingCategory);
    }


    public void deleteCategory(Long id) {

        Category category = categoryRepository
                .findById(id)
                .orElseThrow(() ->
                        new CategoryNotFoundException(
                                "Category with id "
                                        + id
                                        + " not found"
                        )
                );

        categoryRepository.delete(category);
    }
}
