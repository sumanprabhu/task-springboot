package com.task1.suman.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "admin_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminRequest {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    // Who is requesting admin access

    private String status;
    // "PENDING", "APPROVED", "REJECTED"

    private String reason;
    // Why they want admin access

    private LocalDateTime requestedAt;

    private LocalDateTime decidedAt;

    private String decidedBy;
    // Email of the admin who approved/rejected
}