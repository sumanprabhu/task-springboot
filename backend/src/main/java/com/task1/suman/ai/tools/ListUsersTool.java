package com.task1.suman.ai.tools;

import com.task1.suman.ai.tools.ToolRecords.*;
import com.task1.suman.model.User;
import com.task1.suman.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

@Component("listUsersTool")
@Description("Lists all users in the system. Can filter by 'all', 'admins', or 'users'. Returns the count and details of each user.")
public class ListUsersTool
        implements Function<ListUsersRequest, UserListResponse> {

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserListResponse apply(ListUsersRequest request) {
        try {
            List<User> allUsers = userRepo.findAll();

            // Filter based on request
            List<User> filtered;
            String filter = request.filter() != null ?
                    request.filter().toLowerCase() : "all";

            if (filter.equals("admins")) {
                filtered = allUsers.stream()
                        .filter(u -> u.getRole() != null &&
                                u.getRole().getRoleName().equals("ADMIN"))
                        .toList();
            } else if (filter.equals("users")) {
                filtered = allUsers.stream()
                        .filter(u -> u.getRole() != null &&
                                u.getRole().getRoleName().equals("USER"))
                        .toList();
            } else {
                filtered = allUsers;
            }

            // Convert to summaries
            List<UserSummary> summaries = filtered.stream()
                    .map(u -> new UserSummary(
                            u.getName(),
                            u.getEmail(),
                            u.getRole() != null ? u.getRole().getRoleName() : "NONE",
                            u.getAddress() != null ? u.getAddress().getCity() : "No address"
                    ))
                    .toList();

            return new UserListResponse("SUCCESS", summaries.size(), summaries);

        } catch (Exception e) {
            return new UserListResponse("FAILED", 0, List.of());
        }
    }
}