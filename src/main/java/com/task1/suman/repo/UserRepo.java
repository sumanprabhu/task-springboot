package com.task1.suman.repo;

import com.task1.suman.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserRepo extends JpaRepository<User, UUID> {
    List<User> findAllByNameContainingIgnoreCase(String name);
}
