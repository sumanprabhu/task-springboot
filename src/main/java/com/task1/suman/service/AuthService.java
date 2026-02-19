package com.task1.suman.service;

import com.task1.suman.dto.AuthResponse;
import com.task1.suman.dto.LoginRequest;
import com.task1.suman.dto.RegisterRequest;
import com.task1.suman.model.Role;
import com.task1.suman.model.User;
import com.task1.suman.repo.RoleRepo;
import com.task1.suman.repo.UserRepo;
import com.task1.suman.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;
    // ↑ This is the BCryptPasswordEncoder bean we created in SecurityConfig

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;
    // ↑ This verifies email + password (also from SecurityConfig)

    // ============================================
    //  REGISTER — Create a new user
    // ============================================
    public AuthResponse register(RegisterRequest request) {

        // 1. Check if email already exists
        if (userRepo.findByEmail(request.getEmail()).isPresent()) {
            return new AuthResponse(null, "Email already exists!");
            //  ↑ No token, just error message
        }

        // 2. Find the role from database
        Role role = roleRepo.findByRoleName(request.getRoleName())
                .orElseThrow(() -> new RuntimeException(
                        "Role not found: " + request.getRoleName()
                ));
        //  ↑ If someone sends roleName "SUPERADMIN" which doesn't exist
        //    → throw error

        // 3. Create new user
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setContactNum(request.getContactNum());
        user.setRole(role);

        // 4. HASH the password before saving
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        //                ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
        //  "password123" → "$2a$10$N9qo8uLOickgx2ZMRZoMye..."
        //
        //  WHY? If database is hacked, hackers see:
        //  "$2a$10$N9qo8u..." NOT "password123"
        //  BCrypt CANNOT be reversed!

        // 5. Save to database
        userRepo.save(user);

        // 6. Generate JWT token for the new user
        String token = jwtUtil.generateToken(user.getEmail(), role.getRoleName());

        // 7. Return token + success message
        return new AuthResponse(token, "Registration successful!");
    }

    // ============================================
    //  LOGIN — Verify credentials & return token
    // ============================================
    public AuthResponse login(LoginRequest request) {

        // 1. Verify email + password using AuthenticationManager
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        //  ↑↑↑ What happens inside:
        //  1. Finds user by email (calls UserDetailsService/UserRepo)
        //  2. Compares password with hashed password in DB
        //  3. If WRONG → throws BadCredentialsException
        //  4. If CORRECT → continues to next line ✅

        // 2. If we reach here, credentials are CORRECT
        //    Find the user to get their role
        User user = userRepo.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 3. Generate JWT token
        String token = jwtUtil.generateToken(
                user.getEmail(),
                user.getRole().getRoleName()
        );

        // 4. Return token + success message
        return new AuthResponse(token, "Login successful!");
    }
}