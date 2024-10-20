package com.gigster.skymarket.config;


import com.gigster.skymarket.repository.RoleRepository;
import com.gigster.skymarket.repository.UserRepository;
import com.gigster.skymarket.model.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class RoleConfig implements CommandLineRunner {
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    @Override
    public void run(String... args) throws Exception {
        log.info(":::::::::::checking if roles exist, if it doesn't add them::::::::::::");
       String admin="ADMIN";
       String customer="CUSTOMER";

       if(!roleRepository.findByName(admin).isPresent()){
           Role role=new Role();
           role.setName(admin);
           roleRepository.save(role);
       }if(!roleRepository.findByName(customer).isPresent()){
            Role role=new Role();
            role.setName(customer);
            roleRepository.save(role);
        }

    }
}