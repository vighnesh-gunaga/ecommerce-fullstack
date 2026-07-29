package com.example.EcommarceWebsite.controller;

import com.example.EcommarceWebsite.dto.ProductRequest;
import com.example.EcommarceWebsite.dto.ProductResponse;
import com.example.EcommarceWebsite.model.Product;
import com.example.EcommarceWebsite.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {
    @Autowired
    private ProductService productService;
    @GetMapping("/allproducts")
    public List<ProductResponse> getAllProducts()
    {
        return productService.getAllProducts();
    }
    @GetMapping("/productbyid/{id}")
    public ProductResponse getProductById(@PathVariable Long id)
    {
        return productService.getProductById(id);
    }
    @GetMapping("/productbyname/{name}")
    public ProductResponse getProductByName(@PathVariable String name)
    {
        return productService.getProductByName(name);
    }
    @PostMapping("/addproduct")
    public ProductResponse addProduct(@RequestBody ProductRequest productRequest)
    {
        return productService.addProduct(productRequest);
    }
    @PutMapping("/updateproduct/{id}")
    public Product updateProduct(@RequestBody Product product,@PathVariable Long id)
    {
        return productService.updateProduct(product,id);
    }
    @DeleteMapping("/deleteproduct/{id}")
    public String deleteProduct(@PathVariable Long id)
    {
        productService.deleteProduct(id);
        return "Product deleted Successfully";
    }
}
