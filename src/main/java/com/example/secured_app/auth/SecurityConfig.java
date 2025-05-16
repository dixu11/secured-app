package com.example.secured_app.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(customizer -> customizer
                        .requestMatchers(HttpMethod.GET,"/register").permitAll()
                        .requestMatchers(HttpMethod.POST,"/register").permitAll()
                .requestMatchers("/").permitAll()
                .anyRequest().authenticated())
                .csrf(csrf -> csrf.disable())
                .build();
    }
}
