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
                http.csrf().disable()
                 .authorizeHttpRequests((authorize) ->

                        authorize
                                //ALL PERMITTED
                                //Swagger UI
                                .requestMatchers("/swagger-ui/**").permitAll()
                                .requestMatchers("/v3/**").permitAll()
                                // Users
                                .requestMatchers("/api/users/auth/signup").permitAll()
                                .requestMatchers("/api/users/auth/signin").permitAll()
                                .requestMatchers("/api/users/auth/current").hasAnyAuthority("ADMIN", "CUSTOMER")
                                // products permits
                                .requestMatchers(HttpMethod.GET,"/api/products/{id}").permitAll()
                                .requestMatchers(HttpMethod.GET,"/api/products").permitAll()

                                //ADMIN PERMITS
                                //users
                                .requestMatchers("/api/users/auth/update").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.GET,"/api/users/auth").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.GET,"/api/users/auth/id/*").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.GET,"/api/users/auth").hasAuthority("ADMIN")
                                .requestMatchers("/api/users/auth/new/role").hasAuthority("ADMIN")

                                //products
                                .requestMatchers(HttpMethod.PUT,"/api/products/{id}").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.DELETE,"/api/products/{id}").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.POST,"/api/products/{id}").hasAuthority("ADMIN")

                                //customers
                                .requestMatchers(HttpMethod.POST,"/api/customers").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.GET,"/api/customers").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.GET,"/api/customers/{id}").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.DELETE,"/api/customers/{id}").hasAuthority("ADMIN")
                                //Orders

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
