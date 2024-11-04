package com.gigster.skymarket.service.impl;

import com.gigster.skymarket.dto.*;
import com.gigster.skymarket.exception.ResourceNotFoundException;
import com.gigster.skymarket.model.Customer;
import com.gigster.skymarket.model.Role;
import com.gigster.skymarket.model.User;
import com.gigster.skymarket.repository.CustomerRepository;
import com.gigster.skymarket.repository.RoleRepository;
import com.gigster.skymarket.repository.UserRepository;
import com.gigster.skymarket.security.JwtTokenProvider;
import com.gigster.skymarket.service.UserService;
import com.gigster.skymarket.mapper.ResponseDtoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.gigster.skymarket.enums.RoleName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    ResponseDtoMapper responseDtoSetter;


    public UserServiceImpl(AuthenticationManager authenticationManager,
                           UserRepository userRepository,
                           RoleRepository roleRepository,
                           CustomerRepository customerRepository,
                           PasswordEncoder passwordEncoder,
                           JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.customerRepository = customerRepository;

        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }
    @Override
    public ResponseEntity<ResponseDto> register(SignUpRequest signUpRequest) {
        if(userRepository.existsByEmail(signUpRequest.getEmail())) {
            return responseDtoSetter.responseDtoSetter(HttpStatus.NOT_ACCEPTABLE,"Email Address already in use!");
        }
        if(userRepository.existsByUsername(signUpRequest.getUsername())) {
            return responseDtoSetter.responseDtoSetter(HttpStatus.NOT_ACCEPTABLE,"Username is already taken!");
        }
        User user = User.builder()
                .email(signUpRequest.getEmail())
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .fullName(signUpRequest.getFullName())
                .username(signUpRequest.getUsername())
                .contact(signUpRequest.getContact())
                .gender(signUpRequest.getGender())
                .build();
        // By default, when a new user signs up, they are assigned the CUSTOMER Role
        if (roleRepository.findByName(RoleName.CUSTOMER).isPresent()) {
            Optional<Role> userRole = roleRepository.findByName(RoleName.CUSTOMER);

            userRole.ifPresent(role -> user.setRoles(Collections.singleton(role)));
            //todo move this to customer service
            Customer customer = new Customer();
            customer.setFullName(user.getFullName());
            customer.setEmail(user.getEmail());
            customer.setPhoneNo(user.getContact());
            customer.setGender(user.getGender());

            customer.setOrders(new ArrayList<>());

            customerRepository.save(customer);
        }

 log.info("this is the user:::::::::::::::::::::::::::::::::::::::::::::::::::"+ user);

        userRepository.save(user);
        return responseDtoSetter.responseDtoSetter(HttpStatus.ACCEPTED,"User registered successfully");

    }
    @Override
    public String login(LoginDto loginDto) {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getUsername(), loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);


        return jwtTokenProvider.generateToken(authentication);
    }
    @Override
    public ResponseEntity<?> addRole(AddRoleRequest addRoleRequest) {
        Role role=new Role();
        role.setName(RoleName.fromString(addRoleRequest.getName()));
        roleRepository.save(role);
        return null;
    }
    @Override
    public ResponseEntity<ResponseDto> findUserById(long id){

        try{
            //todo ... get
            return responseDtoSetter.responseDtoSetter(HttpStatus.OK,"User info", userRepository.findById(id).get());

        }catch(Exception e){
            return responseDtoSetter.responseDtoSetter(HttpStatus.BAD_REQUEST, "User Not Found");
        }
    }

    @Override
    public ResponseEntity<ResponseDto> getAllUsers(Pageable pageable) {
        Page<User> userPage = userRepository.findAll(pageable);

        List<UserResponseDto> userResponseDtos = userPage.getContent()
                .stream()
                .map(this::mapUserResponseDto)
                .collect(Collectors.toList());

        ResponseDto responseDto = responseDtoSetter.responseDtoSetter(
                HttpStatus.OK,
                "Fetched List of All Users.",
                userResponseDtos
        ).getBody();

        assert responseDto != null;
        responseDto.setTotalPages(userPage.getTotalPages());
        responseDto.setTotalElements(userPage.getTotalElements());
        responseDto.setCurrentPage(pageable.getPageNumber());
        responseDto.setPageSize(pageable.getPageSize());

        return ResponseEntity.ok(responseDto);
    }
    @Override
    public ResponseEntity<?> deleteById(long id){

        try{
            userRepository.deleteById(id);
            return responseDtoSetter.responseDtoSetter(HttpStatus.ACCEPTED,"User deleted successfully");


        }catch(Exception e){
            return responseDtoSetter.responseDtoSetter(HttpStatus.BAD_REQUEST,"User with that id not found");

        }
    }
    @Override
    public ResponseEntity<?> getCurrentUser(String email) {

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
           return responseDtoSetter.responseDtoSetter(HttpStatus.FOUND,"Current User logged in", currentUserDto);


        } catch (ResourceNotFoundException ex) {
            // Handle user not found case
            return responseDtoSetter.responseDtoSetter(HttpStatus.NOT_FOUND,ex.getMessage());


        } catch (Exception ex) {
            // Handle any other unexpected exceptions
            return responseDtoSetter.responseDtoSetter(HttpStatus.INTERNAL_SERVER_ERROR,"An error occurred while fetching the user details");

        }
    }

    private UserResponseDto mapUserResponseDto(User user){
        return UserResponseDto.builder()
                .email(user.getEmail())
                .username(user.getUsername())
//               TODO: build role.
                .build();
    }

}
