package com.gigster.skymarket.config;

import com.gigster.skymarket.enums.RoleName;
import com.gigster.skymarket.model.Role;
import com.gigster.skymarket.repository.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RoleConfig implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public RoleConfig(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) {
        log.info("Checking if roles exist, and adding them if they don't");

        if(roleRepository.findByName(RoleName.ADMIN).isEmpty()) {
            Role role = new Role();
            role.setName(RoleName.ADMIN);
            roleRepository.save(role);
        }

        if(roleRepository.findByName(RoleName.CUSTOMER).isEmpty()) {
            Role role = new Role();
            role.setName(RoleName.CUSTOMER);
            roleRepository.save(role);
        }
    }
}