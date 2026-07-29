package com.example.EcommarceWebsite.security;

import com.example.EcommarceWebsite.model.User;
import com.example.EcommarceWebsite.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    // Constructor injection
    public JwtAuthenticationFilter(
            JwtService jwtService,
            UserRepository userRepository
    ) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // Get Authorization header
        String authHeader = request.getHeader("Authorization");

        // If Authorization header is missing or does not start with Bearer,
        // continue without JWT authentication
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {

            filterChain.doFilter(request, response);
            return;
        }

        // Remove "Bearer " from the token
        String token = authHeader.substring(7);

        try {

            // Extract email from JWT token
            String email = jwtService.extractEmailFromToken(token);

            // Check whether authentication already exists
            if (email != null
                    && SecurityContextHolder
                    .getContext()
                    .getAuthentication() == null) {

                // Find user in the database
                Optional<User> optionalUser =
                        userRepository.findByEmail(email);

                if (optionalUser.isPresent()) {

                    User user = optionalUser.get();

                    // Validate JWT token
                    if (jwtService.isTokenValid(token, user)) {

                        // Convert USER or ADMIN role to:
                        // ROLE_USER or ROLE_ADMIN
                        SimpleGrantedAuthority authority =
                                new SimpleGrantedAuthority(
                                        "ROLE_"
                                                + user.getRole().name()
                                );

                        // Create authenticated user object
                        UsernamePasswordAuthenticationToken
                                authentication =
                                new UsernamePasswordAuthenticationToken(
                                        user,
                                        null,
                                        Collections.singletonList(
                                                authority
                                        )
                                );

                        // Store authentication in Spring Security
                        SecurityContextHolder
                                .getContext()
                                .setAuthentication(
                                        authentication
                                );
                    }
                }
            }

        } catch (Exception exception) {

            // Invalid, expired, malformed, or incorrect JWT token.
            // Do not authenticate the user.
            // Spring Security will handle the request based on SecurityConfig.
            SecurityContextHolder.clearContext();
        }

        // Continue to the next security filter
        filterChain.doFilter(request, response);
    }
}