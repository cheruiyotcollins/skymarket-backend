package com.gigster.skymarket.service;

import com.gigster.skymarket.dto.LoginDto;
import com.gigster.skymarket.dto.ResponseDto;
import com.gigster.skymarket.dto.SignUpRequest;
import com.gigster.skymarket.repository.RoleRepository;
import com.gigster.skymarket.repository.UserRepository;
import com.gigster.skymarket.model.Role;
import com.gigster.skymarket.security.JwtTokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.gigster.skymarket.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.*;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private JwtTokenProvider jwtTokenProvider;

    ResponseDto responseDto;


    public UserService(AuthenticationManager authenticationManager,
                           UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder,
                           JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;

        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public ResponseEntity<?> register(SignUpRequest signUpRequest) {
        if(userRepository.existsByEmail(signUpRequest.getEmail())) {

            return new ResponseEntity<>("Email Address already in use!",
                    HttpStatus.NOT_ACCEPTABLE);
        }
        if(userRepository.existsByUsername(signUpRequest.getUsername())) {
            return new ResponseEntity<>( "Username is already taken!",
                    HttpStatus.NOT_ACCEPTABLE);
        }
        // Creating user's account using lombok builder method
        // todo encrypt password before saving
        User user = User.builder()
                .email(signUpRequest.getEmail())
                .password(signUpRequest.getPassword())
                .name(signUpRequest.getName())
                .username(signUpRequest.getUsername())
                .build();




        if(roleRepository.findByName("CUSTOMER").isPresent()) {
            Role userRole = roleRepository.findByName("CUSTOMER").get();
            user.setRoles(Collections.singleton(userRole));
        }
        userRepository.save(user);
        return new ResponseEntity<>("User registered successfully",HttpStatus.ACCEPTED);
    }

    public String login(LoginDto loginDto) {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getUsername(), loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);


        return jwtTokenProvider.generateToken(authentication);
    }
}
