package com.app.vasyBus.config.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .cors(cors -> {})
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/auth/login",
                                "api/auth/logout",
                                "/api/auth/register",
                                "/api/schedule/search",
                                "/api/schedule/**",
                                "/api/seats/schedule/**"
                        ).permitAll()

                        .requestMatchers(
                                "/api/add/bus",
                                "/api/buses",
                                "/api/bus/**",
                                "/api/update/bus/**",
                                "/api/delete/bus/**",
                                "/api/add/route",
                                "/api/routes",
                                "/api/route/**",
                                "/api/update/route/**",
                                "/api/delete/route/**",
                                "/api/add/schedule",
                                "/api/update/schedule/**",
                                "/api/delete/schedule/**"
                        ).hasRole("ADMIN")

                        .anyRequest().authenticated()
                )

                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                        .accessDeniedHandler(new CustomAccessDeniedHandler())
                )

                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    public static class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

        @Override
        public void commence(HttpServletRequest request,
                             HttpServletResponse response,
                             AuthenticationException authException)
                throws IOException {

            response.sendError(
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "Unauthorized - Invalid or missing token"
            );
        }
    }

    public static class CustomAccessDeniedHandler implements AccessDeniedHandler {

        @Override
        public void handle(HttpServletRequest request,
                           HttpServletResponse response,
                           AccessDeniedException accessDeniedException)
                throws IOException {

            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Forbidden - You do not have permission to access this resource"
            );
        }
    }
}