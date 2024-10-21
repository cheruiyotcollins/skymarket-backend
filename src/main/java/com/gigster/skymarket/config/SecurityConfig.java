package com.gigster.skymarket.config;


import com.gigster.skymarket.security.JwtAuthenticationEntryPoint;
import com.gigster.skymarket.security.JwtAuthenticationFilter;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
@SecurityScheme(
        name = "Bear Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class SecurityConfig {

    private UserDetailsService userDetailsService;
    private final JwtAuthenticationEntryPoint authenticationEntryPoint;
    private final JwtAuthenticationFilter authenticationFilter;

    public SecurityConfig(UserDetailsService userDetailsService,
                          JwtAuthenticationEntryPoint authenticationEntryPoint,
                          JwtAuthenticationFilter authenticationFilter){
        this.userDetailsService = userDetailsService;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.authenticationFilter = authenticationFilter;
    }

    @Bean
    public static PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // Removed .cors() from the chain
        http.authorizeHttpRequests((authorize) ->
                        //all permitted
                        authorize.requestMatchers("/swagger-ui/**").permitAll()
                                .requestMatchers("/v3/**").permitAll()
                                .requestMatchers("/api/user/auth/signup").permitAll()
                                .requestMatchers("/api/user/auth/signin").permitAll()
                                //Admin access only
                                .requestMatchers("/api/user/auth/update").hasAuthority("ADMIN")
                                .requestMatchers("/api/user/auth/add").hasAuthority("ADMIN")
                                .requestMatchers("/api/user/auth/findById/**").hasAuthority("ADMIN")
                                .requestMatchers("/api/user/auth/list/all").hasAuthority("ADMIN")
                                .requestMatchers("/api/user/auth/deleteById/**").hasAuthority("ADMIN")
                                .requestMatchers("/api/user/auth/new/role").hasAuthority("ADMIN")
                                .requestMatchers("/api/user/auth/current").hasAnyAuthority("ADMIN", "CUSTOMER")
                                //Admin and Customer Access
                                .requestMatchers("/selections/schools/list/**").hasAnyAuthority("ADMIN", "CUSTOMER")
                                .requestMatchers("/selections/zones/list/**").hasAnyAuthority("ADMIN", "CUSTOMER")
                                .requestMatchers("/selections/subjects/list/**").hasAnyAuthority("ADMIN", "CUSTOMER")
                                .requestMatchers("/selections/schools/delete/**").hasAnyAuthority("ADMIN", "CUSTOMER")
                                .requestMatchers("/selections/zones/delete/**").hasAnyAuthority("ADMIN", "CUSTOMER")
                                .requestMatchers("/selections/subjects/delete/**").hasAnyAuthority("ADMIN")
                                .requestMatchers("/selections/schools/find/**").hasAnyAuthority("ADMIN", "CUSTOMER")
                                .requestMatchers("/selections/zones/find/**").hasAnyAuthority("ADMIN", "CUSTOMER")
                                .requestMatchers("/selections/subjects/find/**").hasAnyAuthority("ADMIN", "CUSTOMER")
                                //Customer Access
                                .requestMatchers("/selections/schools/new/**").hasAnyAuthority("ADMIN", "CUSTOMER")
                                .requestMatchers("/selections/zones/new/**").hasAuthority("STUDENT")
                                .requestMatchers("/selections/subjects/new/**").hasAuthority("STUDENT")
                                .requestMatchers("/subjects/list/**").hasAnyAuthority("ADMIN", "CUSTOMER")
                                .requestMatchers("/subjects/find/**").hasAnyAuthority("ADMIN", "CUSTOMER")
                                .anyRequest().authenticated()
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authenticationEntryPoint)
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
