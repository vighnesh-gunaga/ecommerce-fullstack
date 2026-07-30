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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private ProductResponse mapToProductResponse(Product product) {

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
    public Page<ProductResponse> getAllProducts(int page, int size,String sortBy,String direction) {

        Sort sort = direction.equalsIgnoreCase("desc")?
                Sort.by(sortBy).descending():
                Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size ,sort);

        Page<Product> productPage = productRepository.findAll(pageable);

        return productPage.map(this::mapToResponse);
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
    private ProductResponse mapToResponse(Product product) {

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
    public ProductResponse updateProduct(ProductRequest product, Long id) {

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

        if (product.getCategoryId() == null) {

            throw new IllegalArgumentException(
                    "Category ID is required"
            );
        }

        Long categoryId = product.getCategoryId();

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

        Product product1 = productRepository.save(existingProduct);

        ProductResponse productResponse = new ProductResponse();
        productResponse.setId(product1.getId());
        productResponse.setName(product1.getName());
        productResponse.setDescription(product1.getDescription());
        productResponse.setPrice(product1.getPrice());
        productResponse.setStockQuantity(product1.getStockQuantity());
        productResponse.setImageUrl(product1.getImageUrl());
        productResponse.setBrand(product1.getBrand());
        productResponse.setCategoryId(product1.getCategory().getId());
        productResponse.setCategoryName(product1.getCategory().getName());
        productResponse.setCreatedAt(product1.getCreatedAt());
        productResponse.setUpdatedAt(product1.getUpdatedAt());
        return productResponse;
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

    public List<ProductResponse> getProductsByCategory(Long categoryId) {
        List<Product> products = productRepository.findByCategoryId(categoryId);

        return products.stream()
                .map(this::mapToProductResponse)
                .toList();
    }

    public List<ProductResponse> searchProduct(String keyword) {

        List<Product> products = productRepository.findByNameContainingIgnoreCase(keyword);
        return products.stream().map(this::mapToProductResponse).toList();
    }

    public List<ProductResponse> getProductByBrand(String brand) {

        List<Product> products = productRepository.findProductByBrandContainingIgnoreCase(brand);
        return products.stream().map(this::mapToProductResponse).toList();
    }

    public List<ProductResponse> getProductByCategory(Long categoryId) {

        List<Product> products = productRepository.findProductByCategoryId(categoryId);
        return products.stream().map(this::mapToProductResponse).toList();
    }

    public List<ProductResponse> getProductsByPrice(double min, double max) {

        List<Product> products = productRepository.findByPriceBetween(min, max);

        return products.stream()
                .map(this::mapToProductResponse)
                .toList();
    }
    public Page<ProductResponse> filterProducts(

            String brand,

            Long categoryId,

            Double minPrice,

            Double maxPrice,

            int page,

            int size,

            String sortBy,

            String direction
    ) {

        Sort sort = direction.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Product> products = productRepository.filterProducts(
                brand,
                categoryId,
                minPrice,
                maxPrice,
                pageable
        );

        return products.map(this::mapToProductResponse);
    }
}
