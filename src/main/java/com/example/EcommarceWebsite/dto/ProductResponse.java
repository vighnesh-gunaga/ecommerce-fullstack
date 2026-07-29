package com.example.EcommarceWebsite.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Integer stockQuantity;
    private String imageUrl;
    private String brand;
    private Long categoryId;
    private String categoryName;
    private LocalDate createdAt;
    private LocalDate updatedAt;
}
