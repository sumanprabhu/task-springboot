package com.task1.suman.controller;

import com.task1.suman.model.AdminRequest;
import com.task1.suman.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    // USER requests admin access
    // POST /admin/request
    @PostMapping("/request")
    public ResponseEntity<AdminRequest> requestAdmin(
            @RequestBody Map<String, String> body,
            Authentication authentication
            //              ↑↑↑↑↑↑↑↑↑↑↑↑↑↑
            //  Spring Security automatically gives us
            //  the logged-in user's details from the token!
    ) {
        String reason = body.get("reason");
        String email = authentication.getName();
        // getName() returns the email because
        // in User.java getUsername() returns email

        AdminRequest request = adminService.requestAdminAccess(email, reason);
        return new ResponseEntity<>(request, HttpStatus.CREATED);
    }

    // ADMIN views pending requests
    // GET /admin/requests
    @GetMapping("/requests")
    public ResponseEntity<List<AdminRequest>> getPendingRequests() {
        return new ResponseEntity<>(adminService.getPendingRequests(), HttpStatus.OK);
    }

    // ADMIN approves request
    // PUT /admin/approve/{requestId}
    @PutMapping("/approve/{requestId}")
    public ResponseEntity<AdminRequest> approve(
            @PathVariable UUID requestId,
            Authentication authentication
    ) {
        String adminEmail = authentication.getName();
        return new ResponseEntity<>(
                adminService.approveRequest(requestId, adminEmail),
                HttpStatus.OK
        );
    }

    // ADMIN rejects request
    // PUT /admin/reject/{requestId}
    @PutMapping("/reject/{requestId}")
    public ResponseEntity<AdminRequest> reject(
            @PathVariable UUID requestId,
            Authentication authentication
    ) {
        String adminEmail = authentication.getName();
        return new ResponseEntity<>(
                adminService.rejectRequest(requestId, adminEmail),
                HttpStatus.OK
        );
    }
}