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
    private JwtAuthenticationEntryPoint authenticationEntryPoint;
    private JwtAuthenticationFilter authenticationFilter;

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
                                .requestMatchers("/user/auth/signup").permitAll()
                                .requestMatchers("/user/auth/signin").permitAll()
                                //Admin access only
                                .requestMatchers("/user/auth/update").hasAuthority("ADMIN")
                                .requestMatchers("/user/auth/add").hasAuthority("ADMIN")
                                .requestMatchers("/user/auth/findById/**").hasAuthority("ADMIN")
                                .requestMatchers("/user/auth/list/all").hasAuthority("ADMIN")
                                .requestMatchers("/user/auth/deleteById/**").hasAuthority("ADMIN")
                                .requestMatchers("/user/auth/new/role").hasAuthority("ADMIN")
                                .requestMatchers("/user/auth/current").hasAnyAuthority("ADMIN", "LECTURER", "STUDENT")
                                //Admin and lecturer Access
                                .requestMatchers("/selections/schools/list/**").hasAnyAuthority("ADMIN", "LECTURER", "STUDENT")
                                .requestMatchers("/selections/zones/list/**").hasAnyAuthority("ADMIN", "LECTURER", "STUDENT")
                                .requestMatchers("/selections/subjects/list/**").hasAnyAuthority("ADMIN", "LECTURER", "STUDENT")
                                .requestMatchers("/selections/schools/delete/**").hasAnyAuthority("ADMIN", "LECTURER")
                                .requestMatchers("/selections/zones/delete/**").hasAnyAuthority("ADMIN", "LECTURER")
                                .requestMatchers("/selections/subjects/delete/**").hasAnyAuthority("ADMIN", "LECTURER")
                                .requestMatchers("/selections/schools/find/**").hasAnyAuthority("ADMIN", "LECTURER", "STUDENT")
                                .requestMatchers("/selections/zones/find/**").hasAnyAuthority("ADMIN", "LECTURER", "STUDENT")
                                .requestMatchers("/selections/subjects/find/**").hasAnyAuthority("ADMIN", "LECTURER", "STUDENT")
                                //Students Access
                                .requestMatchers("/selections/schools/new/**").hasAnyAuthority("ADMIN", "LECTURER", "STUDENT")
                                .requestMatchers("/selections/zones/new/**").hasAuthority("STUDENT")
                                .requestMatchers("/selections/subjects/new/**").hasAuthority("STUDENT")
                                .requestMatchers("/subjects/list/**").hasAnyAuthority("ADMIN", "LECTURER", "STUDENT")
                                .requestMatchers("/subjects/find/**").hasAnyAuthority("ADMIN", "LECTURER", "STUDENT")
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
