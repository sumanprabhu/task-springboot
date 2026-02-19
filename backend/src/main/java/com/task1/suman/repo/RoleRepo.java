package com.task1.suman.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.task1.suman.model.Role;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepo extends JpaRepository<Role, UUID> {
    Optional<Role> findByRoleName(String roleName);
}
