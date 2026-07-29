package com.example.EcommarceWebsite.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CategoryResponse {

    private Long id;
    private String name;
    private String description;
    private LocalDate createdAt;
    private LocalDate updatedAt;
}