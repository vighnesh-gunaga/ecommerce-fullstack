package com.example.EcommarceWebsite.controller;

import com.example.EcommarceWebsite.dto.CategoryRequest;
import com.example.EcommarceWebsite.dto.CategoryResponse;
import com.example.EcommarceWebsite.model.Category;
import com.example.EcommarceWebsite.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @GetMapping("/allcategory")
    public List<CategoryResponse> getAllCategory()
    {
        return categoryService.getAllCategory();
    }
    @GetMapping("/categories/{id}")
    public CategoryResponse getCategoryById(@PathVariable Long id)
    {
        return categoryService.getCategoryById(id);
    }
    @GetMapping("/categories/name/{name}")
    public CategoryResponse getCategoryByName(@PathVariable String name)
    {
        return categoryService.getCategoryByName(name);
    }
    @PostMapping("/addcategory")
    public CategoryResponse addCategory(@Valid @RequestBody CategoryRequest categoryRequest)
    {
        return categoryService.addcategory(categoryRequest);
    }
    @PutMapping("/updatecategory/{id}")
    public CategoryResponse updateCategory(@RequestBody Category category,@PathVariable Long id)
    {
        return categoryService.updateCategory(category,id);
    }
    @DeleteMapping("/deletecategory/{id}")
    public String deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return "Category deleted successfully";
    }
}
