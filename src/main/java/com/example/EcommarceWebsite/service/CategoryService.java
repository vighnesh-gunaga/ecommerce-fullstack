package com.example.EcommarceWebsite.service;

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

    public Category getCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }

    public Category addcategory(Category category) {
        return categoryRepository.save(category);
    }

    public Category updateCategory(Category category, Long id) {

        Category category1 = categoryRepository.findById(id).orElseThrow(()->new CategoryNotFoundException("Not found"));
        category1.setName(category.getName());
        category1.setDescription(category.getDescription());
        category1.setProductsList(category.getProductsList());
        category1.setCreatedAt(category.getCreatedAt());
        category1.setUpdatedAt(category.getUpdatedAt());

        return categoryRepository.save(category1);
    }


    public void deleteCategory(Long id) {
        if(categoryRepository.existsById(id))
        {
            categoryRepository.deleteById(id);
        }
        else
        {
            throw new CategoryNotFoundException("Category with id "+id+" Not Found");
        }
    }
}
