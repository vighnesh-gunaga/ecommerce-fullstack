package com.example.EcommarceWebsite.config;

import com.example.EcommarceWebsite.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    // Constructor injection
    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter
    ) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        return http

                // Disable CSRF because this is a stateless REST API
                .csrf(csrf -> csrf.disable())

                // JWT-based authentication does not use HTTP sessions
                .sessionManagement(session -> session
                        .sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                // Authorization rules
                .authorizeHttpRequests(auth -> auth

                        // Public APIs
                        .requestMatchers(
                                "/api/user/register",
                                "/api/user/login",
                                "/error"
                        ).permitAll()

                        // USER and ADMIN can view products
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/product/**"
                        ).hasAnyRole(
                                "USER",
                                "ADMIN"
                        )

                        // Only ADMIN can add products
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/product/**"
                        ).hasRole("ADMIN")

                        // Only ADMIN can update products
                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/product/**"
                        ).hasRole("ADMIN")

                        // Only ADMIN can delete products
                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/product/**"
                        ).hasRole("ADMIN")

                        // USER and ADMIN can view categories
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/category/**"
                        ).hasAnyRole(
                                "USER",
                                "ADMIN"
                        )

                        // Only ADMIN can add categories
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/category/**"
                        ).hasRole("ADMIN")

                        // Only ADMIN can update categories
                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/category/**"
                        ).hasRole("ADMIN")

                        // Only ADMIN can delete categories
                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/category/**"
                        ).hasRole("ADMIN")

                        // Any other API requires authentication
                        .anyRequest().authenticated()
                )

                // Run JWT filter before Spring's username/password filter
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                )

                .build();
    }
}