package com.task1.suman.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    // What the client sends when registering
    private String name;
    private String email;
    private String password;
    private String contactNum;
    private String roleName;  // Just send "ADMIN" or "USER" as string instead of sending entire Role object!
}
