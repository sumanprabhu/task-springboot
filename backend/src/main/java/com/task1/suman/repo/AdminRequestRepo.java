package com.task1.suman.repo;

import com.task1.suman.model.AdminRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AdminRequestRepo extends JpaRepository<AdminRequest, UUID> {
    List<AdminRequest> findByStatus(String status);
    List<AdminRequest> findByUserId(UUID userId);
}