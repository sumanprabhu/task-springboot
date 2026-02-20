package com.task1.suman.ai.tools;

// These records define WHAT the AI sends to each tool
// and WHAT each tool sends back

// ===== CREATE USER =====
public class ToolRecords {

    // What AI sends to createUser tool
    public record CreateUserRequest(
            String name,
            String email,
            String contactNum
    ) {}

    // What createUser tool sends back to AI
    public record CreateUserResponse(
            String status,
            String message
    ) {}

    // What AI sends to findUser tool
    public record FindUserRequest(
            String email
    ) {}

    // What findUser tool sends back
    public record FindUserResponse(
            String name,
            String email,
            String contactNum,
            String role,
            String city
    ) {}

    // What AI sends to deleteUser tool
    public record DeleteUserRequest(
            String email
    ) {}

    // What AI sends to changeRole tool
    public record ChangeRoleRequest(
            String email,
            String newRole
    ) {}

    // What AI sends to listUsers tool
    public record ListUsersRequest(
            String filter
            // "all", "admins", "users"
    ) {}

    // Generic response for simple operations
    public record SimpleResponse(
            String status,
            String message
    ) {}

    // Each user in list
    public record UserSummary(
            String name,
            String email,
            String role,
            String city
    ) {}

    // List response
    public record UserListResponse(
            String status,
            int count,
            java.util.List<UserSummary> users
    ) {}
}