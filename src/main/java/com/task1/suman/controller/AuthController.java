package com.task1.suman.controller;

import com.task1.suman.dto.AuthResponse;
import com.task1.suman.dto.LoginRequest;
import com.task1.suman.dto.RegisterRequest;
import com.task1.suman.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")  // All endpoints start with /auth
public class AuthController {

    @Autowired
    private AuthService authService;

    // ============================================
    //  POST /auth/register
    // ============================================
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);

        if (response.getToken() == null) {
            // Registration failed (email already exists)
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // ============================================
    //  POST /auth/login
    // ============================================
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}