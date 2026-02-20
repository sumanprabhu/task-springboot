package com.task1.suman.service;

import com.task1.suman.model.AdminRequest;
import com.task1.suman.model.Role;
import com.task1.suman.model.User;
import com.task1.suman.repo.AdminRequestRepo;
import com.task1.suman.repo.RoleRepo;
import com.task1.suman.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class AdminService {

    @Autowired
    private AdminRequestRepo adminRequestRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RoleRepo roleRepo;

    // USER requests admin access
    public AdminRequest requestAdminAccess(String userEmail, String reason) {

        User user = userRepo.findByEmail(userEmail)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found"
                ));

        // Check if already admin
        if (user.getRole().getRoleName().equals("ADMIN")) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "You are already an ADMIN!"
            );
        }

        // Check if already has a pending request
        List<AdminRequest> existing = adminRequestRepo.findByUserId(user.getId());
        boolean hasPending = existing.stream()
                .anyMatch(r -> r.getStatus().equals("PENDING"));

        if (hasPending) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "You already have a pending request!"
            );
        }

        // Create new request
        AdminRequest request = new AdminRequest();
        request.setUser(user);
        request.setReason(reason);
        request.setStatus("PENDING");
        request.setRequestedAt(LocalDateTime.now());

        return adminRequestRepo.save(request);
    }

    // ADMIN views all pending requests
    public List<AdminRequest> getPendingRequests() {
        return adminRequestRepo.findByStatus("PENDING");
    }

    // ADMIN approves a request
    public AdminRequest approveRequest(UUID requestId, String adminEmail) {

        AdminRequest request = adminRequestRepo.findById(requestId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Request not found"
                ));

        if (!request.getStatus().equals("PENDING")) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Request already " + request.getStatus()
            );
        }

        // Change user's role to ADMIN
        User user = request.getUser();
        Role adminRole = roleRepo.findByRoleName("ADMIN")
                .orElseThrow(() -> new RuntimeException("ADMIN role not found"));

        user.setRole(adminRole);
        userRepo.save(user);

        // Update request status
        request.setStatus("APPROVED");
        request.setDecidedAt(LocalDateTime.now());
        request.setDecidedBy(adminEmail);

        return adminRequestRepo.save(request);
    }

    // ADMIN rejects a request
    public AdminRequest rejectRequest(UUID requestId, String adminEmail) {

        AdminRequest request = adminRequestRepo.findById(requestId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Request not found"
                ));

        if (!request.getStatus().equals("PENDING")) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Request already " + request.getStatus()
            );
        }

        request.setStatus("REJECTED");
        request.setDecidedAt(LocalDateTime.now());
        request.setDecidedBy(adminEmail);

        return adminRequestRepo.save(request);
    }
}