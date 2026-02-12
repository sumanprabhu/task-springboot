package com.task1.suman.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.task1.suman.model.Role;

import java.util.UUID;

public interface RoleRepo extends JpaRepository<Role, UUID> {
}
