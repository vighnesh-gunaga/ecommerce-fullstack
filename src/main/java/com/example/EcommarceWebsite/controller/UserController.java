package com.example.EcommarceWebsite.controller;

import com.example.EcommarceWebsite.dto.LoginRequest;
import com.example.EcommarceWebsite.dto.LoginResponse;
import com.example.EcommarceWebsite.dto.UserRequest;
import com.example.EcommarceWebsite.dto.UserResponse;
import com.example.EcommarceWebsite.model.User;
import com.example.EcommarceWebsite.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public UserResponse register(@RequestBody UserRequest userRequest)
    {
        return userService.register(userRequest);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest)
    {
        return userService.login(loginRequest);
    }


}
