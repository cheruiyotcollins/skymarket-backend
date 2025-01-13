package com.gigster.skymarket.service.impl;

import com.gigster.skymarket.dto.*;
import com.gigster.skymarket.exception.ResourceNotFoundException;
import com.gigster.skymarket.mapper.UserMapper;
import com.gigster.skymarket.model.Customer;
import com.gigster.skymarket.model.Role;
import com.gigster.skymarket.model.User;
import com.gigster.skymarket.repository.CustomerRepository;
import com.gigster.skymarket.repository.RoleRepository;
import com.gigster.skymarket.repository.UserRepository;
import com.gigster.skymarket.security.JwtTokenProvider;
import com.gigster.skymarket.security.UserPrincipal;
import com.gigster.skymarket.service.NotificationService;
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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.gigster.skymarket.enums.RoleName;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;
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
    private final NotificationService notificationService;

    @Autowired
    ResponseDtoMapper responseDtoSetter;

    @Autowired
    UserMapper userMapper;


    public UserServiceImpl(AuthenticationManager authenticationManager,
                           UserRepository userRepository,
                           RoleRepository roleRepository,
                           CustomerRepository customerRepository,
                           PasswordEncoder passwordEncoder,
                           JwtTokenProvider jwtTokenProvider,
                           NotificationService notificationService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.customerRepository = customerRepository;

        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.notificationService=notificationService;
    }

    @Override
    @Transactional
    public ResponseEntity<ResponseDto> register(SignUpRequestDto signUpRequestDto) {
        // Check if email or username already exists
        if (userRepository.existsByEmail(signUpRequestDto.getEmail())) {
            return responseDtoSetter.responseDtoSetter(HttpStatus.NOT_ACCEPTABLE, "Email Address already in use!");
        }
        if (userRepository.existsByUsername(signUpRequestDto.getUsername())) {
            return responseDtoSetter.responseDtoSetter(HttpStatus.NOT_ACCEPTABLE, "Username is already taken!");
        }

        // Determine if the user is an admin
        boolean isAdmin = signUpRequestDto.getRoleName() != null && signUpRequestDto.getRoleName().equalsIgnoreCase("ROLE_ADMIN");

        if (isAdmin) {
            // Create the User without a Customer entity for admin users
            User adminUser = User.builder()
                    .email(signUpRequestDto.getEmail())
                    .password(passwordEncoder.encode(signUpRequestDto.getPassword()))
                    .fullName(signUpRequestDto.getFullName())
                    .username(signUpRequestDto.getUsername())
                    .contact(signUpRequestDto.getContact())
                    .gender(signUpRequestDto.getGender())
                    .firstLogin(true)
                    .build();

            // Assign the admin role
            Role adminRole = roleRepository.findByName(RoleName.valueOf("ROLE_ADMIN"))
                    .orElseThrow(() -> new RuntimeException("Admin role not found!"));
            adminUser.setRoles(Set.of(adminRole));

            // Save the Admin User
            userRepository.save(adminUser);

            log.info("Registered admin: {}", adminUser);
            return responseDtoSetter.responseDtoSetter(HttpStatus.ACCEPTED, "Admin registered successfully");
        }

        // For normal users (customers), create the Customer entity first
        Customer customer = new Customer();
        customer.setFullName(signUpRequestDto.getFullName());
        customer.setEmail(signUpRequestDto.getEmail());
        customer.setPhoneNo(signUpRequestDto.getContact());
        customer.setGender(signUpRequestDto.getGender());

        // Persist the Customer entity
        customer = customerRepository.save(customer);

        // Ensure Customer is saved successfully
        if (customer.getCustomerId() == null) {
            return responseDtoSetter.responseDtoSetter(HttpStatus.INTERNAL_SERVER_ERROR, "Customer could not be created.");
        }

        // Now create the User entity and associate it with the Customer
        User user = User.builder()
                .email(signUpRequestDto.getEmail())
                .password(passwordEncoder.encode(signUpRequestDto.getPassword()))
                .fullName(signUpRequestDto.getFullName())
                .username(signUpRequestDto.getUsername())
                .contact(signUpRequestDto.getContact())
                .gender(signUpRequestDto.getGender())
                .customer(customer) // Link the Customer to the User
                .firstLogin(false)
                .build();

        // Assign ROLE_CUSTOMER by default or another role based on request
        String roleName = signUpRequestDto.getRoleName() != null ? signUpRequestDto.getRoleName() : "ROLE_CUSTOMER";
        Role role = roleRepository.findByName(RoleName.valueOf(roleName.toUpperCase()))
                .orElseThrow(() -> new RuntimeException("Role not found!"));

        user.setRoles(Set.of(role));

        // Save the User entity
        userRepository.save(user);

        // Log both User and Customer
        log.info("Registered user: {}, Associated customer: {}", user, customer);

        return responseDtoSetter.responseDtoSetter(HttpStatus.ACCEPTED, "User registered successfully");
    }

    @Override
    public LoginResponseDto login(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        User user = userRepository.findById(userPrincipal.getId()).orElseThrow();

        // Check if this is the user's first login
        boolean firstLogin = user.getFirstLogin();

        // If not the first login, generate a token
        String token =jwtTokenProvider.generateToken(authentication);

        return new LoginResponseDto(token, firstLogin);
    }

    @Override
    public ResponseEntity<ResponseDto> addRole(AddRoleRequestDto addRoleRequestDto) {
        Role role=new Role();
        role.setName(RoleName.fromString(addRoleRequestDto.getName()));
        roleRepository.save(role);
        return null;
    }

    @Override
    public ResponseEntity<ResponseDto> findUserById(long id) {

        try {
            Optional<User> userOptional = userRepository.findById(id);

            if (userOptional.isPresent()) {
                return responseDtoSetter.responseDtoSetter(HttpStatus.OK, "User info", userOptional.get());
            } else {
                return responseDtoSetter.responseDtoSetter(HttpStatus.NOT_FOUND, "User Not Found");
            }

        } catch (Exception e) {
            return responseDtoSetter.responseDtoSetter(HttpStatus.BAD_REQUEST, "An error occurred while retrieving user info");
        }
    }

    @Override
    public ResponseEntity<ResponseDto> getAllUsers(Pageable pageable) {
        Page<User> usersPage = userRepository.findAll(pageable);
        List<UserDto> userDtos = usersPage.getContent()
                .stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());

        ResponseDto responseDto = ResponseDto.builder()
                .status(HttpStatus.OK)
                .description("List of All Users.")
                .payload(userDtos)
                .totalPages(usersPage.getTotalPages())
                .totalElements(usersPage.getTotalElements())
                .currentPage(usersPage.getNumber())
                .pageSize(usersPage.getSize())
                .build();

        return ResponseEntity.ok(responseDto);
    }

    @Override
    public ResponseEntity<ResponseDto> deleteById(long id){

        try{
            userRepository.deleteById(id);
            return responseDtoSetter.responseDtoSetter(HttpStatus.ACCEPTED,"User deleted successfully");


        }catch(Exception e){
            return responseDtoSetter.responseDtoSetter(HttpStatus.BAD_REQUEST,"User with that id not found");

        }
    }

    @Override
    public ResponseEntity<ResponseDto> getCurrentUser(String email) {

        try {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User with email " + email + "not found"));

            CurrentUserDto currentUserDto = CurrentUserDto.builder()
                    .name(user.getUsername())
                    .email(user.getEmail())
                    .role(user.getRoles()
                            .stream()
                            .map(role -> role.getName().getRoleName())
                            .collect(Collectors.joining(", "))) // Join the roles into a single String
                    .build();

           return responseDtoSetter.responseDtoSetter(HttpStatus.FOUND,"Current User logged in", currentUserDto);

        } catch (ResourceNotFoundException ex) {
            return responseDtoSetter.responseDtoSetter(HttpStatus.NOT_FOUND,ex.getMessage());

        } catch (Exception ex) {
            return responseDtoSetter.responseDtoSetter(HttpStatus.INTERNAL_SERVER_ERROR,"An error occurred while fetching the user details");
        }
    }

    @Override
    public ResponseEntity<ResponseDto> updatePassword(String newPassword, Principal principal) {
        User user = userRepository.findByUsername(principal.getName()).orElseThrow();
        user.setPassword(passwordEncoder.encode(newPassword));
        // user first login is being set to false here
        user.setFirstLogin(false);
        userRepository.save(user);
        return responseDtoSetter.responseDtoSetter(HttpStatus.OK,"Password updated successfully.");
    }

    @Override
    public ResponseEntity<ResponseDto> forgotPassword(String email) {
        // Find the user by email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        // Generate a verification code (e.g., a 6-digit code)
        String verificationCode = String.format("%06d", new Random().nextInt(999999));

        // Save the verification code and expiration time to the user's account
        user.setResetCode(verificationCode);
        user.setResetCodeExpiry(LocalDateTime.now().plusMinutes(15)); // Code expires in 15 minutes
        userRepository.save(user);

        // Send email with the code
        String subject = "Password Reset Request";
        String message = "Your password reset code is " + verificationCode + ". This code expires in 15 minutes.";
        notificationService.sendMail(email, subject, message);

        return responseDtoSetter.responseDtoSetter(HttpStatus.OK,"Password reset code sent to email.");
    }

    @Override
    public ResponseEntity<ResponseDto> resetPassword(String email, String resetCode, String newPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        if (!user.getResetCode().equals(resetCode) || LocalDateTime.now().isAfter(user.getResetCodeExpiry())) {
            return responseDtoSetter.responseDtoSetter(HttpStatus.BAD_REQUEST,"Invalid or expired reset code.");
        }

        // Update the user's password
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetCode(null);
        user.setResetCodeExpiry(null);
        userRepository.save(user);

        return responseDtoSetter.responseDtoSetter(HttpStatus.OK,"Password has been successfully reset.");
    }

    private UserResponseDto mapUserResponseDto(User user){
        return UserResponseDto.builder()
                .email(user.getEmail())
                .username(user.getUsername())
                .roles(user.getRoles().stream()
                        .map(Role::getName)
                        .collect(Collectors.toSet()))
                .build();
    }

}