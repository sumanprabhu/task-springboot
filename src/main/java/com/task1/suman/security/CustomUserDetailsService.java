package com.task1.suman.security;

import com.task1.suman.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    //                                            ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
    //  Spring Security interface that says:
    //  "Tell me HOW to load a user by their username"

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        //  Spring Security calls this method during login
        //  It passes the email (username) and expects a UserDetails object back

        return userRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User not found with email: " + email
                ));
        //  ↑↑↑
        //  Remember: Our User class implements UserDetails
        //  So returning a User object IS returning UserDetails!
        //
        //  Flow:
        //  1. Spring Security calls loadUserByUsername("suman@gmail.com")
        //  2. We search database for this email
        //  3. Found → return User object (which IS UserDetails)
        //  4. Not found → throw exception → 401 Unauthorized
    }
}