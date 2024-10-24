package com.gigster.skymarket.service;

import com.gigster.skymarket.dto.*;
import com.gigster.skymarket.exception.ResourceNotFoundException;
import com.gigster.skymarket.model.Customer;
import com.gigster.skymarket.repository.RoleRepository;
import com.gigster.skymarket.repository.UserRepository;
import com.gigster.skymarket.model.Role;
import com.gigster.skymarket.security.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class UserService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

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

    public ResponseEntity<ResponseDto> register(SignUpRequest signUpRequest) {
        responseDto= new ResponseDto();
        if(userRepository.existsByEmail(signUpRequest.getEmail())) {
            responseDto.setStatus(HttpStatus.NOT_ACCEPTABLE);
            responseDto.setDescription("Email Address already in use!");
            return new ResponseEntity<>(responseDto, responseDto.getStatus());
        }
        if(userRepository.existsByUsername(signUpRequest.getUsername())) {
            responseDto.setStatus(HttpStatus.NOT_ACCEPTABLE);
            responseDto.setDescription("Username is already taken!");
            return new ResponseEntity<>(responseDto, responseDto.getStatus());
        }
        // todo encrypt password before saving
        User user = User.builder()
                .email(signUpRequest.getEmail())
                .password(signUpRequest.getPassword())
                .name(signUpRequest.getName())
                .username(signUpRequest.getUsername())
                .build();
        // by default new user signup he/she is assigned CUSTOMER Role
        if(roleRepository.findByName("CUSTOMER").isPresent()) {
            Role userRole = roleRepository.findByName("CUSTOMER").get();
            user.setRoles(Collections.singleton(userRole));
            //todo then create a new customer
//            Customer customer= new Customer();

        }
        userRepository.save(user);
        responseDto.setStatus(HttpStatus.ACCEPTED);
        responseDto.setDescription("User registered successfully");
        return new ResponseEntity<>(responseDto, responseDto.getStatus());
    }

    public String login(LoginDto loginDto) {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getUsername(), loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);


        return jwtTokenProvider.generateToken(authentication);
    }

    public ResponseEntity<?> addRole(AddRoleRequest addRoleRequest) {
        Role role=new Role();
        role.setName(addRoleRequest.getName());
        roleRepository.save(role);
        return null;
    }

    public ResponseEntity<?> findUserById(long id){

        try{

            return new ResponseEntity<>( userRepository.findById(id).get(),HttpStatus.OK);

        }catch(Exception e){
            responseDto.setStatus(HttpStatus.BAD_REQUEST);
            responseDto.setDescription("User Not Found");
            return new ResponseEntity<>(responseDto,HttpStatus.BAD_REQUEST);
        }
    }
    //todo pagination

    public ResponseEntity<?> findAll(){

        try{

            return new ResponseEntity<>( userRepository.findAll(),HttpStatus.OK);

        }catch(Exception e){
            responseDto.setStatus(HttpStatus.BAD_REQUEST);
            responseDto.setDescription("No User Found");
            return new ResponseEntity<>(responseDto,HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> deleteById(long id){

        try{
            responseDto.setStatus(HttpStatus.ACCEPTED);
            responseDto.setDescription("User deleted successfully");
            userRepository.deleteById(id);
            return new ResponseEntity<>(responseDto,HttpStatus.OK);

        }catch(Exception e){
            responseDto.setStatus(HttpStatus.BAD_REQUEST);
            responseDto.setDescription("User with that id not found");
            return new ResponseEntity<>(responseDto,HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> getCurrentUser(String email) {
        responseDto = new ResponseDto();

        try {
            // Fetch user by email, handle if user not found
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User with email " + email + "not found"));

            // Build the CurrentUserDto object
            CurrentUserDto currentUserDto = CurrentUserDto.builder()
                    .name(user.getUsername())
                    .email(user.getEmail())
                    .role(user.getRoles().iterator().next().getId())
                    .build();

            // Set success response details
            responseDto.setStatus(HttpStatus.FOUND);
            responseDto.setDescription("Current User logged in");
            responseDto.setPayload(currentUserDto);

            return new ResponseEntity<>(responseDto, responseDto.getStatus());

        } catch (ResourceNotFoundException ex) {
            // Handle user not found case
            responseDto.setStatus(HttpStatus.NOT_FOUND);
            responseDto.setDescription(ex.getMessage());
            return new ResponseEntity<>(responseDto, responseDto.getStatus());

        } catch (Exception ex) {
            // Handle any other unexpected exceptions
            responseDto.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            responseDto.setDescription("An error occurred while fetching the user details");
            return new ResponseEntity<>(responseDto, responseDto.getStatus());
        }
    }

}
