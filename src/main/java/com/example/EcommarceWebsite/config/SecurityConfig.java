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

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
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

        http
                // REST API + JWT
                .csrf(csrf -> csrf.disable())

                // JWT is stateless
                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                .authorizeHttpRequests(auth -> auth

                        // ==============================
                        // PUBLIC
                        // ==============================

                        .requestMatchers(
                                "/api/user/register",
                                "/api/user/login",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/error"
                        ).permitAll()


                        // ==============================
                        // PRODUCT
                        // ==============================

                        // USER + ADMIN → GET
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/product/**"
                        ).hasAnyRole("USER", "ADMIN")

                        // ADMIN → POST
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/product/**"
                        ).hasRole("ADMIN")

                        // ADMIN → PUT
                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/product/**"
                        ).hasRole("ADMIN")

                        // ADMIN → DELETE
                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/product/**"
                        ).hasRole("ADMIN")


                        // ==============================
                        // CATEGORY
                        // ==============================

                        // USER + ADMIN → GET
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/category/**"
                        ).hasAnyRole("USER", "ADMIN")

                        // ADMIN → POST
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/category/**"
                        ).hasRole("ADMIN")

                        // ADMIN → PUT
                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/category/**"
                        ).hasRole("ADMIN")

                        // ADMIN → DELETE
                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/category/**"
                        ).hasRole("ADMIN")


                        // ==============================
                        // EVERYTHING ELSE
                        // ==============================

                        // Cart, orders, wishlist, etc.
                        // automatically require authentication
                        .anyRequest().authenticated()
                )

                // JWT filter runs before Spring's authentication filter
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}