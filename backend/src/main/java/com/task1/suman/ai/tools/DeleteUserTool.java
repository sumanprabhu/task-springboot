package com.task1.suman.ai.tools;

import com.task1.suman.ai.tools.ToolRecords.*;
import com.task1.suman.model.User;
import com.task1.suman.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component("deleteUserTool")
@Description("Deletes a user from the system by their email address. This action is permanent and cannot be undone.")
public class DeleteUserTool
        implements Function<DeleteUserRequest, SimpleResponse> {

    @Autowired
    private UserRepo userRepo;

    @Override
    public SimpleResponse apply(DeleteUserRequest request) {
        try {
            User user = userRepo.findByEmail(request.email()).orElse(null);

            if (user == null) {
                return new SimpleResponse("FAILED",
                        "User with email " + request.email() + " not found!");
            }

            userRepo.delete(user);

            return new SimpleResponse("SUCCESS",
                    "User " + user.getName() + " (" + request.email() + ") has been deleted.");

        } catch (Exception e) {
            return new SimpleResponse("FAILED", "Error: " + e.getMessage());
        }
    }
}