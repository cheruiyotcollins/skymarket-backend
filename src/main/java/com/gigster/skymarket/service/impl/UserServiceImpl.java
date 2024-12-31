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
    public ResponseEntity<ResponseDto> register(SignUpRequest signUpRequest) {
        // Check if email or username already exists
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return responseDtoSetter.responseDtoSetter(HttpStatus.NOT_ACCEPTABLE, "Email Address already in use!");
        }
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return responseDtoSetter.responseDtoSetter(HttpStatus.NOT_ACCEPTABLE, "Username is already taken!");
        }

        // Create the User with basic details
        User user = User.builder()
                .email(signUpRequest.getEmail())
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .fullName(signUpRequest.getFullName())
                .username(signUpRequest.getUsername())
                .contact(signUpRequest.getContact())
                .gender(signUpRequest.getGender())
                .firstLogin(false)
                .build();

        // Determine role based on SignUpRequest's roleName or assign ROLE_CUSTOMER by default
        String roleName = signUpRequest.getRoleName() != null ? signUpRequest.getRoleName() : "ROLE_CUSTOMER";
        Role role = roleRepository.findByName(RoleName.valueOf(roleName.toUpperCase()))
                .orElseThrow(() -> new RuntimeException("Role not found!"));

        // Assign the role to the user
        user.setRoles(Set.of(role));

        // Additional logic if the role is CUSTOMER
        if ("ROLE_CUSTOMER".equalsIgnoreCase(roleName)) {
            Customer customer = new Customer();
            customer.setCustomerId(user.getCustomerId());
            customer.setFullName(user.getFullName());
            customer.setEmail(user.getEmail());
            customer.setPhoneNo(user.getContact());
            customer.setGender(user.getGender());
            customer.setOrders(new ArrayList<>());

            customerRepository.save(customer);
        }

        // Save user to the repository
        userRepository.save(user);

        log.info("Registered user: {}", user);

        return responseDtoSetter.responseDtoSetter(HttpStatus.ACCEPTED, "User registered successfully");
    }

    @Override
    public LoginResponse login(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        User user = userRepository.findById(userPrincipal.getId()).orElseThrow();

        // Check if this is the user's first login
        boolean firstLogin = user.getFirstLogin();

        // If not the first login, generate a token
        String token = firstLogin ? null : jwtTokenProvider.generateToken(authentication);

        return new LoginResponse(token, firstLogin);
    }

    @Override
    public ResponseEntity<?> addRole(AddRoleRequest addRoleRequest) {
        Role role=new Role();
        role.setName(RoleName.fromString(addRoleRequest.getName()));
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
    public ResponseEntity<?> updatePassword(String newPassword, Principal principal) {
        User user = userRepository.findByUsername(principal.getName()).orElseThrow();
        user.setPassword(passwordEncoder.encode(newPassword));
        // user first login is being set to false here
        user.setFirstLogin(false);
        userRepository.save(user);
        return responseDtoSetter.responseDtoSetter(HttpStatus.OK,"Password updated successfully");
    }

    @Override
    public ResponseEntity<?> forgotPassword(String email) {
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

        return ResponseEntity.ok("Password reset code sent to email.");
    }

    @Override
    public ResponseEntity<?> resetPassword(String email, String resetCode, String newPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        if (!user.getResetCode().equals(resetCode) || LocalDateTime.now().isAfter(user.getResetCodeExpiry())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired reset code.");
        }

        // Update the user's password
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetCode(null);
        user.setResetCodeExpiry(null);
        userRepository.save(user);

        return ResponseEntity.ok("Password has been successfully reset.");
    }

    private UserResponseDto mapUserResponseDto(User user){
        return UserResponseDto.builder()
                .email(user.getEmail())
                .username(user.getUsername())
//               TODO: build role.
                .build();
    }

}