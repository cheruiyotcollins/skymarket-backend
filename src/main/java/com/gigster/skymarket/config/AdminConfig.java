package com.gigster.skymarket.config;

import com.gigster.skymarket.enums.RoleName;
import com.gigster.skymarket.model.Role;
import com.gigster.skymarket.model.User;
import com.gigster.skymarket.repository.RoleRepository;
import com.gigster.skymarket.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@Slf4j
public class AdminConfig implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminConfig(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        log.info("Checking if an admin user exists");

        boolean noAdminsExist = userRepository.countByRoleName(RoleName.ROLE_ADMIN) == 0;

        if (noAdminsExist) {
            // Check if the ROLE_ADMIN role exists
            Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN).orElse(null);
            if (adminRole == null) {
                log.error("ROLE_ADMIN does not exist in the database. Please insert it manually.");
                return;  // Exit early if the role is not found
            }

            User user = new User();
            Set<Role> roles = new HashSet<>();
            roles.add(adminRole);  // Add the admin role
            user.setRoles(roles);

            user.setFullName("Admin");
            user.setEmail("admin@gmail.com");
            user.setUsername("admin");
            user.setContact("+254700000000");
            user.setPassword(passwordEncoder.encode("password"));
            user.setGender("Undefined");
            user.setFirstLogin(true); // Add this flag to User entity
            userRepository.save(user);
            log.info("Admin user created with first login flag set to true.");
        }
    }
}
