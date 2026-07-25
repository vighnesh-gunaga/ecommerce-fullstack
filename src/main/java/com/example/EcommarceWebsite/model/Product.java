package com.example.EcommarceWebsite.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private double price;
    private Integer stockQuantity;
    private String imageUrl;
    private String brand;
    @ManyToOne
    private Category category;
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDate createdAt;
    @LastModifiedDate
    private LocalDate updatedAt;

}
