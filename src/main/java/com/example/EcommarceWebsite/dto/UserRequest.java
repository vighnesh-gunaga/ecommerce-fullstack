package com.example.EcommarceWebsite.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserRequest {

    @NotBlank(
            message = "Username is required"
    )
    private String username;

    @NotBlank(
            message = "Email is required"
    )
    @Email(
            message = "Enter a valid email"
    )
    private String email;

    @NotBlank(
            message = "Password is required"
    )
    @Size(
            min = 8,
            message =
                    "Password must contain at least 8 characters"
    )
    private String password;
}