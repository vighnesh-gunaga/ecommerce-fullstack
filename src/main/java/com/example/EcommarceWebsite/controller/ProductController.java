package com.example.EcommarceWebsite.controller;

import com.example.EcommarceWebsite.dto.ProductRequest;
import com.example.EcommarceWebsite.dto.ProductResponse;
import com.example.EcommarceWebsite.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {
    @Autowired
    private ProductService productService;
    @GetMapping("/allproducts")
    public Page<ProductResponse> getAllProducts(
            @RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id")String sortBy,@RequestParam(defaultValue = "asc")String direction)
    {
        return productService.getAllProducts(page,size,sortBy,direction);
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
    public ProductResponse updateProduct(@RequestBody ProductRequest product,@PathVariable Long id)
    {
        return productService.updateProduct(product,id);
    }
    @DeleteMapping("/deleteproduct/{id}")
    public String deleteProduct(@PathVariable Long id)
    {
        productService.deleteProduct(id);
        return "Product deleted Successfully";
    }


    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ProductResponse>> getProductsByCategory(
            @PathVariable Long categoryId) {

        return ResponseEntity.ok(productService.getProductsByCategory(categoryId));
    }
    @GetMapping("/search")
    public List<ProductResponse> searchProduct(
            @RequestParam String keyword
    )
    {
        return productService.searchProduct(keyword);
    }
    @GetMapping("/brand")
    public List<ProductResponse> getProductByBrand(@RequestParam String brand)
    {
        return productService.getProductByBrand(brand);
    }
    @GetMapping("/category")
    public List<ProductResponse> getProductByCategory(@PathVariable long categoryId)
    {
        return productService.getProductByCategory(categoryId);
    }
    @GetMapping("/price")
    public List<ProductResponse> getProductByPriceRange(@RequestParam double min,@RequestParam double max)
    {
        return productService.getProductsByPrice(min,max);
    }
    @GetMapping("/filter")
    public Page<ProductResponse> filterProducts(

            @RequestParam(required = false) String brand,

            @RequestParam(required = false) Long categoryId,

            @RequestParam(required = false) Double minPrice,

            @RequestParam(required = false) Double maxPrice,

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "5") int size,

            @RequestParam(defaultValue = "id") String sortBy,

            @RequestParam(defaultValue = "asc") String direction
    ) {

        return productService.filterProducts(
                brand,
                categoryId,
                minPrice,
                maxPrice,
                page,
                size,
                sortBy,
                direction
        );
    }
}
