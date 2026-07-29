package com.example.EcommarceWebsite.dto;

import com.example.EcommarceWebsite.model.Category;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

import javax.net.ssl.SSLSession;

@Getter
@Setter
public class ProductRequest {

    @NotBlank(message = "Product Name is Required")
    private String name;
    @NotBlank(message = "Product Description is Required")
    private String description;
    @NotNull(message = "Price is Required")
    @Positive(message = "Price must be greater than 0")
    private double price;
    @NotNull(message = "Stock quantity is Required")
    @PositiveOrZero(message = "Stock quantity cannot be negative")
    private Integer stockQuantity;
    private String imageUrl;
    @NotBlank(message = "Product Brand is required")
    private String brand;
    @NotNull(message = "Category ID is required")
    private Long categoryId;

}
