package com.ansley.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    // interface = PasswordEncoder
    // new BCryptPasswordEncoder() creates a new object (an instance) of the BCryptPasswordEncoder class.
    // BCryptPasswordEncoder is a class that implements the PasswordEncoder interface.

    // When a request comes in, Spring Security checks all SecurityFilterChain beans,
    // applies the rules, and decides whether to allow or block the request.
    
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // disable CSRF for Postman testing
                .authorizeHttpRequests(requests -> requests
            .requestMatchers("/api/users/register", "/api/users/login",
            	    "/api/buildings/**").permitAll() // allow register/login
            .anyRequest().authenticated()
                        )
        .httpBasic(httpBasic -> {});
        http.headers(headers -> headers.frameOptions().disable());

        return http.build();
    }
    
    @Bean
    WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:3000",
                        		"https://complexes-frontend.vercel.app") // your live frontend) // your Vite dev server
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowCredentials(true);
            }
        };
    }

}
