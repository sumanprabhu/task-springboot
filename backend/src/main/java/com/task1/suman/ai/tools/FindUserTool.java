package com.task1.suman.ai.tools;

import com.task1.suman.ai.tools.ToolRecords.*;
import com.task1.suman.model.User;
import com.task1.suman.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component("findUserTool")
@Description("Finds a user by their email address and returns their details including name, email, contact number, role, and city.")
public class FindUserTool
        implements Function<FindUserRequest, FindUserResponse> {

    @Autowired
    private UserRepo userRepo;

    @Override
    public FindUserResponse apply(FindUserRequest request) {
        try {
            User user = userRepo.findByEmail(request.email()).orElse(null);

            if (user == null) {
                return new FindUserResponse(
                        "NOT FOUND", request.email(),
                        null, null, null
                );
            }

            return new FindUserResponse(
                    user.getName(),
                    user.getEmail(),
                    user.getContactNum(),
                    user.getRole() != null ? user.getRole().getRoleName() : "NONE",
                    user.getAddress() != null ? user.getAddress().getCity() : "No address"
            );

        } catch (Exception e) {
            return new FindUserResponse(
                    "ERROR", e.getMessage(),
                    null, null, null
            );
        }
    }
}