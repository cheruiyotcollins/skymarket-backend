package com.gigster.skymarket.config;

import com.gigster.skymarket.security.JwtAuthenticationEntryPoint;
import com.gigster.skymarket.security.JwtAuthenticationFilter;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
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

    private final UserDetailsService userDetailsService;
    private final JwtAuthenticationEntryPoint authenticationEntryPoint;
    private final JwtAuthenticationFilter authenticationFilter;

    public SecurityConfig(UserDetailsService userDetailsService,
                          JwtAuthenticationEntryPoint authenticationEntryPoint,
                          JwtAuthenticationFilter authenticationFilter) {
        this.userDetailsService = userDetailsService;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.authenticationFilter = authenticationFilter;
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorize) ->
                        authorize
                                // Publicly accessible endpoints.
                                .requestMatchers("/swagger-ui/**").permitAll()
                                .requestMatchers("/v3/**").permitAll()
                                .requestMatchers("/api/v1/users/auth/signup").permitAll()
                                .requestMatchers("/api/v1/users/auth/signin").permitAll()
                                .requestMatchers("/api/v1/products/{id}").permitAll()
                                .requestMatchers("/api/v1/products").permitAll()

                                // Endpoints requiring specific roles.
                                //  User endpoints.
                                .requestMatchers("/api/v1/users/auth/current").hasAnyAuthority("ADMIN", "CUSTOMER")
                                .requestMatchers("/api/v1/users/auth/update").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/v1/users/auth").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/v1/users/auth/id/*").hasAuthority("ADMIN")
                                .requestMatchers("/api/v1/users/auth/new/role").hasAuthority("ADMIN")
                                //  Product endpoints.
                                .requestMatchers(HttpMethod.PUT, "/api/v1/products/{id}").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/api/v1/products/{id}").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.POST, "/api/v1/products/{id}").hasAuthority("ADMIN")
                                //  Customer endpoints.
                                .requestMatchers(HttpMethod.POST, "/api/v1/customers").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/v1/customers").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/v1/customers/{id}").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/api/v1/customers/{id}").hasAuthority("ADMIN")
                                //  Cart items endpoints.
                                .requestMatchers(HttpMethod.POST, "/api/v1/carts/items").hasAnyAuthority("ADMIN", "CUSTOMER")
                                //  Order endpoints.
                                .requestMatchers(HttpMethod.POST, "/api/v1/orders").hasAnyAuthority("ADMIN", "CUSTOMER")
                                .requestMatchers(HttpMethod.GET, "/api/v1/orders/{id}").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/v1/orders").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/api/v1/orders/{id}").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.PATCH, "/api/v1/orders/{id}").hasAnyAuthority("ADMIN", "CUSTOMER")
                                .requestMatchers(HttpMethod.DELETE, "/api/v1/orders/{id}").hasAuthority("ADMIN")

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
