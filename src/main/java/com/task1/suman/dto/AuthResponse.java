package com.task1.suman.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    // Response sent by the server after login
    private String token; // JWT Token
    private String message; // success_message
}
