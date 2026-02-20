package com.task1.suman.ai.tools;

import com.task1.suman.ai.tools.ToolRecords.*;
import com.task1.suman.model.Role;
import com.task1.suman.model.User;
import com.task1.suman.repo.RoleRepo;
import com.task1.suman.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component("changeRoleTool")
@Description("Changes the role of a user. The email of the user and the new role name (ADMIN or USER) must be provided.")
public class ChangeRoleTool
        implements Function<ChangeRoleRequest, SimpleResponse> {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RoleRepo roleRepo;

    @Override
    public SimpleResponse apply(ChangeRoleRequest request) {
        try {
            User user = userRepo.findByEmail(request.email()).orElse(null);

            if (user == null) {
                return new SimpleResponse("FAILED",
                        "User with email " + request.email() + " not found!");
            }

            Role newRole = roleRepo.findByRoleName(request.newRole().toUpperCase())
                    .orElse(null);

            if (newRole == null) {
                return new SimpleResponse("FAILED",
                        "Role " + request.newRole() + " not found! Use ADMIN or USER.");
            }

            user.setRole(newRole);
            userRepo.save(user);

            return new SimpleResponse("SUCCESS",
                    user.getName() + "'s role changed to " + request.newRole().toUpperCase());

        } catch (Exception e) {
            return new SimpleResponse("FAILED", "Error: " + e.getMessage());
        }
    }
}