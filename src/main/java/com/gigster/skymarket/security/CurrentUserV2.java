package com.gigster.skymarket.security;

import com.gigster.skymarket.enums.RoleName;
import com.gigster.skymarket.model.Role;
import com.gigster.skymarket.model.User;
import com.gigster.skymarket.model.Customer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Slf4j
@Service
public class CurrentUserV2 {

    public static UserPrincipal getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserPrincipal) {
            log.debug("Successfully fetched UserPrincipal from SecurityContext: {}", principal);
            return (UserPrincipal) principal;
        } else {
            log.error("Unexpected principal type in SecurityContext: {}", principal.getClass().getName());
            throw new IllegalStateException("Unexpected principal type: " + principal.getClass().getName());
        }
    }

    public static User mapToUser(UserPrincipal userPrincipal) {
        if (userPrincipal == null) {
            log.error("UserPrincipal cannot be null when mapping to User.");
            throw new IllegalStateException("UserPrincipal cannot be null");
        }

        User user = new User();
        user.setUserId(userPrincipal.getId());
        user.setFullName(userPrincipal.getName());
        user.setUsername(userPrincipal.getUsername());
        user.setEmail(userPrincipal.getEmail());
        user.setPassword(userPrincipal.getPassword());

        // Initialize roles if null
        if (user.getRoles() == null) {
            user.setRoles(new HashSet<>());
        }

        // Map authorities to roles
        if (userPrincipal.getAuthorities() != null) {
            userPrincipal.getAuthorities().forEach(authority -> {
                Role role = new Role();
                role.setName(RoleName.valueOf(authority.getAuthority()));
                user.getRoles().add(role);
            });
        } else {
            log.error("UserPrincipal authorities are null.");
            throw new IllegalStateException("UserPrincipal authorities cannot be null");
        }

        log.debug("Mapped UserPrincipal to User: {}", user);
        return user;
    }

    public static Customer mapToCustomer(UserPrincipal userPrincipal) {
        if (userPrincipal == null) {
            log.error("UserPrincipal cannot be null when mapping to Customer.");
            throw new IllegalStateException("UserPrincipal cannot be null");
        }

        Customer customer = new Customer();
        customer.setCustomerId(userPrincipal.getCustomerId());
        customer.setFullName(userPrincipal.getName());
        customer.setEmail(userPrincipal.getEmail());
        customer.setPhoneNo(userPrincipal.getPhoneNo());

        // Validate that Customer ID is not null
        if (customer.getCustomerId() == null) {
            log.error("Customer ID is null after mapping UserPrincipal to Customer. UserPrincipal: {}", userPrincipal);
            throw new IllegalStateException("Customer ID cannot be null");
        }

        log.debug("Mapped UserPrincipal to Customer: {}", customer);
        return customer;
    }
}
