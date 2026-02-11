package com.task1.suman.service;

import com.task1.suman.model.User;
import com.task1.suman.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    public List<User> getUsers() {
        return userRepo.findAll();
    }

    public User getUser(UUID id) {
        return userRepo.findById(id)
                .orElseThrow(
                        ()->new ResponseStatusException(HttpStatus.NOT_FOUND,"user not found with the id :")
                );
    }

    public User addUser(User user) {
        return userRepo.save(user);
    }

    public User updateUser(UUID id,User user) {
        User existingUser = userRepo.findById(id)
                .orElseThrow(
                    ()->new ResponseStatusException(HttpStatus.NOT_FOUND,"user not found with the id :")
                );
        existingUser.setName(user.getName());
        existingUser.setEmail(user.getEmail());
        existingUser.setContactNum(user.getContactNum());
        return userRepo.save(existingUser);
    }

    public void deleteUser(UUID id) {
        userRepo.deleteById(id);
    }

    public List<User> getUserByName(String name) {
        return userRepo.findAllByNameContainingIgnoreCase(name);
    }
}
