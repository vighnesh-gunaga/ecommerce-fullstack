package com.example.EcommarceWebsite.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemResponse {

    private Long cartItemId;

    private Long productId;

    private String productName;

    private String imageUrl;

    private Double price;

    private Integer quantity;

    private Double totalPrice;
}
