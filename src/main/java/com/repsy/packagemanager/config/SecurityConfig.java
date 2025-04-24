package com.repsy.packagemanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/packages/**/download").permitAll()
                .requestMatchers("/api/packages/**").authenticated()
                .anyRequest().permitAll()
            )
            .httpBasic(basic -> {})
            .csrf(csrf -> csrf.disable()); // Disable CSRF for simplicity in this example

        return http.build();
    }
} 