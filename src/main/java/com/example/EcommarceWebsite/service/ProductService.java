package com.example.EcommarceWebsite.service;

import com.example.EcommarceWebsite.dto.ProductRequest;
import com.example.EcommarceWebsite.dto.ProductResponse;
import com.example.EcommarceWebsite.exception.CategoryNotFoundException;
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

    public List<ProductResponse> getAllProducts() {

        List<Product> products = productRepository.findAll();

        return products.stream()
                .map(product -> {

                    ProductResponse response = new ProductResponse();

                    response.setId(product.getId());
                    response.setName(product.getName());
                    response.setDescription(product.getDescription());
                    response.setPrice(product.getPrice());
                    response.setStockQuantity(product.getStockQuantity());
                    response.setImageUrl(product.getImageUrl());
                    response.setBrand(product.getBrand());

                    response.setCategoryId(product.getCategory().getId());
                    response.setCategoryName(product.getCategory().getName());

                    response.setCreatedAt(product.getCreatedAt());
                    response.setUpdatedAt(product.getUpdatedAt());

                    return response;
                })
                .toList();
    }

    public ProductResponse getProductById(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ProductNotFoundException("Product not found"));

        ProductResponse response = new ProductResponse();

        response.setId(product.getId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());
        response.setStockQuantity(product.getStockQuantity());
        response.setImageUrl(product.getImageUrl());
        response.setBrand(product.getBrand());

        response.setCategoryId(product.getCategory().getId());
        response.setCategoryName(product.getCategory().getName());

        response.setCreatedAt(product.getCreatedAt());
        response.setUpdatedAt(product.getUpdatedAt());

        return response;
    }

    public ProductResponse getProductByName(String name) {
        Product product = productRepository.findByName(name).orElseThrow(()->new ProductNotFoundException("Product with name "+name+" Not found"));
        ProductResponse response = new ProductResponse();

        response.setId(product.getId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());
        response.setStockQuantity(product.getStockQuantity());
        response.setImageUrl(product.getImageUrl());
        response.setBrand(product.getBrand());

        response.setCategoryId(product.getCategory().getId());
        response.setCategoryName(product.getCategory().getName());

        response.setCreatedAt(product.getCreatedAt());
        response.setUpdatedAt(product.getUpdatedAt());

        return response;
    }

    public ProductResponse addProduct(ProductRequest productRequest) {

        if (productRequest.getCategoryId() == null) {
            throw new IllegalArgumentException("Category ID is required");
        }

        Long categoryId = productRequest.getCategoryId();

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() ->
                        new CategoryNotFoundException(
                                "Category with id " + categoryId + " not found"
                        ));

        Product product = new Product();
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());
        product.setStockQuantity(productRequest.getStockQuantity());
        product.setImageUrl(productRequest.getImageUrl());
        product.setBrand(productRequest.getBrand());
        product.setCategory(category);

        Product savedProduct = productRepository.save(product);

        ProductResponse productResponse = new ProductResponse();
        productResponse.setId(savedProduct.getId());
        productResponse.setName(savedProduct.getName());
        productResponse.setDescription(savedProduct.getDescription());
        productResponse.setPrice(savedProduct.getPrice());
        productResponse.setStockQuantity(savedProduct.getStockQuantity());
        productResponse.setImageUrl(savedProduct.getImageUrl());
        productResponse.setBrand(savedProduct.getBrand());
        productResponse.setCategoryId(savedProduct.getCategory().getId());
        productResponse.setCategoryName(savedProduct.getCategory().getName());
        productResponse.setCreatedAt(savedProduct.getCreatedAt());
        productResponse.setUpdatedAt(savedProduct.getUpdatedAt());

        return productResponse;
    }

    public Product updateProduct(Product product, Long id) {

        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() ->
                        new ProductNotFoundException(
                                "Product with id "
                                        + id
                                        + " not found"
                        )
                );

        existingProduct.setName(product.getName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setStockQuantity(
                product.getStockQuantity()
        );
        existingProduct.setImageUrl(product.getImageUrl());
        existingProduct.setBrand(product.getBrand());

        if (product.getCategory() == null
                || product.getCategory().getId() == null) {

            throw new IllegalArgumentException(
                    "Category ID is required"
            );
        }

        Long categoryId = product.getCategory().getId();

        Category category = categoryRepository
                .findById(categoryId)
                .orElseThrow(() ->
                        new CategoryNotFoundException(
                                "Category with id "
                                        + categoryId
                                        + " not found"
                        )
                );

        existingProduct.setCategory(category);

        return productRepository.save(existingProduct);
    }

    public void deleteProduct(Long id) {

        Product product = productRepository
                .findById(id)
                .orElseThrow(() ->
                        new ProductNotFoundException(
                                "Product with id "
                                        + id
                                        + " not found"
                        )
                );

        productRepository.delete(product);
    }
}
