package com.task1.suman.ai.tools;

import com.task1.suman.ai.tools.ToolRecords.*;
import com.task1.suman.model.Role;
import com.task1.suman.model.User;
import com.task1.suman.repo.RoleRepo;
import com.task1.suman.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component("createUserTool")
//           ↑↑↑↑↑↑↑↑↑↑↑↑↑↑
//  This name is how we register it with the AI agent

@Description("Creates a new user in the system with the given name, email, and contact number. The user is assigned USER role by default and a temporary password.")
//              ↑↑↑↑↑↑↑↑↑↑↑
//  This description helps AI UNDERSTAND what this tool does
//  AI reads this to decide WHEN to call this tool!
//  Write it clearly — it's like explaining to a human colleague

public class CreateUserTool
        implements Function<CreateUserRequest, CreateUserResponse> {
    //                      ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑    ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
    //                      What AI SENDS       What tool RETURNS

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public CreateUserResponse apply(CreateUserRequest request) {
        // This is YOUR regular Java code!
        // Same logic you'd write in any service

        try {
            // ✅ Validate REQUIRED fields in YOUR code
            if (request.name() == null || request.name().isBlank()) {
                return new CreateUserResponse("MISSING_INFO",
                        "Please provide the user's name.");
            }

            if (request.email() == null || request.email().isBlank()
                    || request.email().contains("example.com")) {
                return new CreateUserResponse("MISSING_INFO",
                        "Please provide a valid email address for " + request.name());
            }

            if (request.contactNum() == null || request.contactNum().isBlank()) {
                return new CreateUserResponse("MISSING_INFO",
                        "Please provide a contact number for " + request.name());
            }

            // Check if email already exists
            if (userRepo.findByEmail(request.email()).isPresent()) {
                return new CreateUserResponse("FAILED",
                        "User with email " + request.email() + " already exists!");
            }

            // Find USER role
            Role role = roleRepo.findByRoleName("USER")
                    .orElseThrow(() -> new RuntimeException("USER role not found"));

            // Create user
            User user = new User();
            user.setName(request.name());
            user.setEmail(request.email());
            user.setContactNum(request.contactNum());
            user.setRole(role);
            user.setPassword(passwordEncoder.encode("temp1234"));

            userRepo.save(user);

            return new CreateUserResponse("SUCCESS",
                    "User " + request.name() + " created successfully with email "
                            + request.email() + ". Default password: temp1234");

        } catch (Exception e) {
            return new CreateUserResponse("FAILED", "Error: " + e.getMessage());
        }
    }
}