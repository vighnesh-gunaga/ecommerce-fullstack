package com.example.EcommarceWebsite.dto;

import com.example.EcommarceWebsite.model.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private Role role;
    private LocalDate createdAt;
    private LocalDate updatedAt;
}
