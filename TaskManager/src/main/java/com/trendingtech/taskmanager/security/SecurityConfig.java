package com.trendingtech.taskmanager.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod; // 1. IMPORTE O HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.Customizer;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            
            .authorizeHttpRequests(authorize -> authorize
                
                // 2. PERMITE O CADASTRO (POST /api/users) SEM TOKEN
                .requestMatchers(HttpMethod.POST, "/api/users").permitAll() 
                
                // 3. PROTEGE TODO O RESTO DENTRO DE /api/
                .requestMatchers("/api/**").authenticated() 
                
                // 4. Permite outras rotas (como Actuator, Swagger, etc.)
                .anyRequest().permitAll() 
            )
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults())); 

        return http.build();
    }
}