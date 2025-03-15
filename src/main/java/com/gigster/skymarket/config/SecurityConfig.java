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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

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
        http.cors(cors -> cors.configurationSource(corsConfigurationSource())) // ✅ Apply CORS config
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorize) ->
                             authorize
                                // Publicly accessible endpoints.
                                .requestMatchers("/swagger-ui/**").permitAll()
                                .requestMatchers("/v3/**").permitAll()
                                .requestMatchers("/api/v1/users/auth/signup").permitAll()
                                .requestMatchers("/api/v1/users/auth/signin").permitAll()
                                .requestMatchers("/api/v1/products/{id}").permitAll()
                                .requestMatchers("/api/v1/products").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/v1/carts**").permitAll()
                                     .requestMatchers(HttpMethod.DELETE, "/api/v1/carts**").permitAll()
                                     .requestMatchers(HttpMethod.POST, "/api/v1/orders**").permitAll()
                                     .requestMatchers(HttpMethod.GET, "/api/v1/orders/**").permitAll()

                                // Endpoints requiring specific roles.
                                //  User endpoints.
                                .requestMatchers("/api/v1/users/auth/current").hasAnyAuthority("ROLE_SUPER_ADMIN","ROLE_ADMIN", "ROLE_CUSTOMER")
                                .requestMatchers("/api/v1/users/auth/update").hasAnyAuthority("ROLE_SUPER_ADMIN", "ROLE_ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/v1/users/auth").hasAnyAuthority("ROLE_SUPER_ADMIN","ROLE_ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/v1/users/auth/id/*").hasAnyAuthority("ROLE_SUPER_ADMIN","ROLE_ADMIN")
                                .requestMatchers("/api/v1/users/auth/new/role").hasAnyAuthority("ROLE_SUPER_ADMIN","ROLE_ADMIN")

                                //  Product endpoints.
                                .requestMatchers(HttpMethod.PUT, "/api/v1/products/{id}").hasAnyAuthority("ROLE_SUPER_ADMIN","ROLE_ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/api/v1/products/{id}").hasAnyAuthority("ROLE_SUPER_ADMIN","ROLE_ADMIN")
                                .requestMatchers(HttpMethod.POST, "/api/v1/products/{id}").hasAnyAuthority("ROLE_SUPER_ADMIN","ROLE_ADMIN")

                                //  Customer endpoints.
                                .requestMatchers(HttpMethod.POST, "/api/v1/customers").hasAnyAuthority("ROLE_SUPER_ADMIN","ROLE_ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/v1/customers").hasAnyAuthority("ROLE_SUPER_ADMIN","ROLE_ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/v1/customers/{id}").hasAnyAuthority("ROLE_SUPER_ADMIN","ROLE_ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/api/v1/customers/{id}").hasAnyAuthority("ROLE_SUPER_ADMIN","ROLE_ADMIN")

                                //  Cart endpoints.
//                                .requestMatchers(HttpMethod.GET, "/api/v1/carts").hasAnyAuthority("ROLE_SUPER_ADMIN","ROLE_ADMIN")
//                                .requestMatchers(HttpMethod.GET, "/api/v1/carts/customer").hasAnyAuthority("ROLE_SUPER_ADMIN","ROLE_ADMIN","ROLE_CUSTOMER")


                                //  Cart items endpoints.
//                                .requestMatchers(HttpMethod.POST, "/api/v1/carts/items").hasAnyAuthority("ROLE_SUPER_ADMIN", "ROLE_ADMIN", "ROLE_CUSTOMER")

                                //  Order endpoints.
                                .requestMatchers(HttpMethod.POST, "/api/v1/orders").hasAnyAuthority("ROLE_SUPER_ADMIN", "ROLE_ADMIN", "ROLE_CUSTOMER")
                                .requestMatchers(HttpMethod.GET, "/api/v1/orders/{id}").hasAnyAuthority("ROLE_SUPER_ADMIN","ROLE_ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/v1/orders").hasAnyAuthority("ROLE_SUPER_ADMIN","ROLE_ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/api/v1/orders/{id}").hasAnyAuthority("ROLE_SUPER_ADMIN","ROLE_ADMIN")
                                .requestMatchers(HttpMethod.PATCH, "/api/v1/orders/{id}").hasAnyAuthority("ROLE_SUPER_ADMIN", "ROLE_ADMIN", "ROLE_CUSTOMER")
                                .requestMatchers(HttpMethod.DELETE, "/api/v1/orders/{id}").hasAnyAuthority("ROLE_SUPER_ADMIN","ROLE_ADMIN")

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
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:3000"); // Allow your frontend
        configuration.addAllowedMethod("*"); // Allow all HTTP methods
        configuration.addAllowedHeader("*"); // Allow all headers
        configuration.setAllowCredentials(true); // Allow credentials
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
