package com.gigster.skymarket.config;

import com.gigster.skymarket.enums.RoleName;
import com.gigster.skymarket.model.User;
import com.gigster.skymarket.repository.RoleRepository;
import com.gigster.skymarket.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

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

        boolean noAdminsExist = userRepository.countByRoleName(RoleName.ADMIN) == 0;

        if (noAdminsExist) {
            User user = new User();
            user.setRole(roleRepository.findByName(RoleName.ADMIN).orElseThrow());
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
