package com.example.EcommarceWebsite.repository;

import com.example.EcommarceWebsite.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product,Long> {
    Optional<Product> findByName(String name);

    List<Product> findByCategoryId(Long categoryId);

    boolean existsByCategoryId(Long id);

    List<Product> findByNameContainingIgnoreCase(String keyword);


    List<Product> findProductByBrandContainingIgnoreCase(String brand);


    List<Product> findProductByCategoryId(Long categoryId);

    List<Product> findByPriceBetween(double min, double max);

    @Query("""
            SELECT p
            FROM Product p
            WHERE
            (:brand IS NULL OR LOWER(p.brand) = LOWER(:brand))
            AND
            (:categoryId IS NULL OR p.category.id = :categoryId)
            AND
            (:minPrice IS NULL OR p.price >= :minPrice)
            AND
            (:maxPrice IS NULL OR p.price <= :maxPrice)
            """)
    Page<Product> filterProducts(

            @Param("brand") String brand,

            @Param("categoryId") Long categoryId,

            @Param("minPrice") Double minPrice,

            @Param("maxPrice") Double maxPrice,

            Pageable pageable
    );
}
