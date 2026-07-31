package com.example.EcommarceWebsite.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class CartResponse {

    private Long cartId;

    private Long userId;

    private String username;

    private List<CartItemResponse> items;

    private Integer totalItems;

    private Double totalAmount;

    private LocalDate createdAt;

    private LocalDate updatedAt;
}