package com.gigster.skymarket.service;

import com.gigster.skymarket.dto.ResponseDto;
import com.gigster.skymarket.dto.SignUpRequest;
import com.gigster.skymarket.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.gigster.skymarket.model.User;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public ResponseEntity<?> register(SignUpRequest signUpRequest) {
        if(userRepository.existsByEmail(signUpRequest.getEmail())) {

            return new ResponseEntity("Email Address already in use!",
                    HttpStatus.NOT_ACCEPTABLE);
        }
        if(userRepository.existsByUsername(signUpRequest.getUsername())) {
            return new ResponseEntity( "Username is already taken!",
                    HttpStatus.NOT_ACCEPTABLE);
        }
        // todo signup using this methods automatically assigns a user as a customer

        // Creating user's account using lombok builder method
        // todo encrypt password before saving
        User user = User.builder()
                .email(signUpRequest.getEmail())
                .password(signUpRequest.getPassword())
                .fullName(signUpRequest.getName())
                .username(signUpRequest.getUsername())
                .build();

        //todo use enums instead of table to store roles


//        if(roleRepository.findByName("STUDENT").isPresent()){
//            Role userRole = roleRepository.findByName("STUDENT").get();
//            user.setRoles(Collections.singleton(userRole));
//        }
        userRepository.save(user);

        return new ResponseEntity<>("User registered successfully",HttpStatus.ACCEPTED);
    }

}
