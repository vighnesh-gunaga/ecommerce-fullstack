package com.example.EcommarceWebsite.service;

import com.example.EcommarceWebsite.exception.ProductNotFoundException;
import com.example.EcommarceWebsite.model.Category;
import com.example.EcommarceWebsite.model.Product;
import com.example.EcommarceWebsite.repository.CategoryRepository;
import com.example.EcommarceWebsite.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(()->new ProductNotFoundException("Not found"));
    }

    public Product getProductByName(String name) {
        return productRepository.findByName(name);
    }

    public Product addProduct(Product product) {
        Long categoryId = product.getCategory().getId();
        Category category = categoryRepository.findById(categoryId).orElseThrow(()->new RuntimeException("" +
                "Category with id "+categoryId+" Not found"));
        product.setCategory(category);
        return productRepository.save(product);
    }

    public Product updateProduct(Product product, Long id) {
        Product product1 = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(
                        "Product with id " + id + " Not Found"
                ));

        product1.setName(product.getName());
        product1.setDescription(product.getDescription());
        product1.setPrice(product.getPrice());
        product1.setStockQuantity(product.getStockQuantity());
        product1.setImageUrl(product.getImageUrl());
        product1.setBrand(product.getBrand());

        // Get category from database
        Long categoryId = product.getCategory().getId();

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException(
                        "Category with id " + categoryId + " Not Found"
                ));

        product1.setCategory(category);

        return productRepository.save(product1);
    }

    public void deleteProduct(Long id) {
        if(productRepository.existsById(id))
        {
            productRepository.deleteById(id);
        }
        else
        {
            throw new ProductNotFoundException("Product with id "+id+" Not found");
        }
    }
}
