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



    public UserResponse register(UserRequest userRequest) {

        String email = userRequest.getEmail();
        if(userRepository.existsByEmail(email))
        {
            throw new EmailAlreadyExistsException("User with email "+email+ "Already Registered");
        }
        User user = new User();

        user.setUsername(userRequest.getUsername());
        user.setEmail(userRequest.getEmail());

        String encodePassword = passwordEncoder.encode(userRequest.getPassword());

        user.setPassword(encodePassword);

        user.setRole(Role.USER);

        User savedUser = userRepository.save(user);

        UserResponse userResponse = new UserResponse();

        userResponse.setId(savedUser.getId());
        userResponse.setUsername(savedUser.getUsername());
        userResponse.setEmail(savedUser.getEmail());
        userResponse.setRole(savedUser.getRole());
        userResponse.setCreatedAt(savedUser.getCreatedAt());
        userResponse.setUpdatedAt(savedUser.getUpdatedAt());

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
        return new LoginResponse();
    }
}
