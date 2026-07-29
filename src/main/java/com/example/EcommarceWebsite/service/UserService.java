package com.example.EcommarceWebsite.service;

import com.example.EcommarceWebsite.dto.LoginRequest;
import com.example.EcommarceWebsite.dto.LoginResponse;
import com.example.EcommarceWebsite.dto.UserRequest;
import com.example.EcommarceWebsite.dto.UserResponse;
import com.example.EcommarceWebsite.exception.EmailAlreadyExistsException;
import com.example.EcommarceWebsite.exception.InvalidCredentialException;
import com.example.EcommarceWebsite.model.Role;
import com.example.EcommarceWebsite.model.User;
import com.example.EcommarceWebsite.repository.UserRepository;
import com.example.EcommarceWebsite.security.JwtService;
import io.jsonwebtoken.Jwt;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;



    public UserResponse register(
            UserRequest userRequest
    ) {

        String email = userRequest
                .getEmail()
                .trim()
                .toLowerCase();

        if (userRepository.existsByEmail(email)) {

            throw new EmailAlreadyExistsException(
                    "User with email "
                            + email
                            + " is already registered"
            );
        }

        User user = new User();

        user.setUsername(
                userRequest.getUsername()
        );

        user.setEmail(email);

        user.setPassword(
                passwordEncoder.encode(
                        userRequest.getPassword()
                )
        );

        // Every newly registered user gets USER role
        user.setRole(Role.USER);

        // Save user in the database
        User savedUser =
                userRepository.save(user);

        // Create response object
        UserResponse userResponse =
                new UserResponse();

        userResponse.setId(
                savedUser.getId()
        );

        userResponse.setUsername(
                savedUser.getUsername()
        );

        userResponse.setEmail(
                savedUser.getEmail()
        );

        userResponse.setRole(
                savedUser.getRole()
        );

        userResponse.setCreatedAt(
                savedUser.getCreatedAt()
        );

        userResponse.setUpdatedAt(
                savedUser.getUpdatedAt()
        );

        // Return the response
        return userResponse;
    }


    public LoginResponse login(LoginRequest loginRequest) {

        String email = loginRequest.getEmail();
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if(optionalUser.isEmpty())
        {
            throw new InvalidCredentialException("Invalid email or password");
        }
        User user = optionalUser.get();
        if(!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword()))
        {
            throw new InvalidCredentialException("Invalid Email or password");
        }
        String token = jwtService.generateToken(user);
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(token);
        return loginResponse;
    }
}
