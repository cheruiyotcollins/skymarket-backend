package com.gigster.skymarket.security;

import com.gigster.skymarket.model.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserV2 {
    public static UserPrincipal getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserPrincipal) {
            return (UserPrincipal) principal;
        } else {
            throw new IllegalStateException("Unexpected principal type");
        }
    }
    public static User mapToUser(UserPrincipal userPrincipal) {
        User user = new User();
        user.setId(userPrincipal.getId());
        user.setFullName(userPrincipal.getName());
        user.setUsername(userPrincipal.getUsername());
        user.setEmail(userPrincipal.getEmail());
        user.setPassword(userPrincipal.getPassword());
        user.setRoleId(userPrincipal.getRoleId());
        return user;
    }
}
