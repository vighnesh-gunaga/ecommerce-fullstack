package com.example.EcommarceWebsite.repository;

import com.example.EcommarceWebsite.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Long> {
    Product findByName(String name);
}
