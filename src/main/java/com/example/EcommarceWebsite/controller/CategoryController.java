package com.example.EcommarceWebsite.controller;

import com.example.EcommarceWebsite.model.Category;
import com.example.EcommarceWebsite.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @GetMapping("/allcategory")
    public List<Category> getAllCategory()
    {
        return categoryService.getAllCategory();
    }
    @GetMapping("/categories/{id}")
    public Category getCategoryById(@PathVariable Long id)
    {
        return categoryService.getCategoryById(id);
    }
    @GetMapping("/categories/name/{name}")
    public Category getCategoryByName(@PathVariable String name)
    {
        return categoryService.getCategoryByName(name);
    }
    @PostMapping("/addcategory")
    public Category addCategory(@RequestBody Category category)
    {
        return categoryService.addcategory(category);
    }
    @PutMapping("/updatecategory/{id}")
    public Category updateCategory(@RequestBody Category category,@PathVariable Long id)
    {
        return categoryService.updateCategory(category,id);
    }
    @DeleteMapping("/deletecategory/{id}")
    public String deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return "Category deleted successfully";
    }
}
