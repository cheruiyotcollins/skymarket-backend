package com.gigster.skymarket.security;

import com.gigster.skymarket.enums.RoleName;
import com.gigster.skymarket.model.Role;
import com.gigster.skymarket.model.User;
import com.gigster.skymarket.model.Customer;
import org.springframework.security.core.GrantedAuthority;
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

        // Get the first authority and map it to the user's role
        GrantedAuthority authority = userPrincipal.getAuthorities().stream().findFirst().orElse(null);
        if (authority != null) {
            Role role = new Role();
            role.setName(RoleName.valueOf(authority.getAuthority()));
            user.setRole(role);
        }

        return user;
    }

    public static Customer mapToCustomer(UserPrincipal userPrincipal) {
        Customer customer = new Customer();
        customer.setId(userPrincipal.getId());
        customer.setFullName(userPrincipal.getName());
        customer.setEmail(userPrincipal.getEmail());
        customer.setPhoneNo(userPrincipal.getPhoneNo());
        return customer;
    }
}
