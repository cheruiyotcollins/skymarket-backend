package com.gigster.skymarket.security;

import com.gigster.skymarket.enums.RoleName;
import com.gigster.skymarket.model.Role;
import com.gigster.skymarket.model.User;
import com.gigster.skymarket.model.Customer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashSet;

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

        // Initialize the roles set if it's null
        if (user.getRoles() == null) {
            user.setRoles(new HashSet<>());
        }

        // Iterate over the authorities and add each role to the roles set
        userPrincipal.getAuthorities().forEach(authority -> {
            Role role = new Role();
            role.setName(RoleName.valueOf(authority.getAuthority())); // Convert authority to RoleName
            user.getRoles().add(role);  // Add the role to the user's roles set
        });

        return user;
    }

    public static Customer mapToCustomer(UserPrincipal userPrincipal) {
        if (userPrincipal == null) {
            throw new IllegalStateException("UserPrincipal cannot be null");
        }

        // Create a new Customer and populate it using the UserPrincipal
        Customer customer = new Customer();
        customer.setCustomerId(userPrincipal.getId());
        customer.setFullName(userPrincipal.getName());
        customer.setEmail(userPrincipal.getEmail());
        customer.setPhoneNo(userPrincipal.getPhoneNo());

        return customer;
    }
}
