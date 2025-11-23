package com.trendingtech.taskmanager.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod; // <--- IMPORTANTE: Adicione este import
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Desabilita CSRF (Padrão e necessário para APIs REST stateless)
            .csrf(AbstractHttpConfigurer::disable)
            
            .authorizeHttpRequests(auth -> auth
                // ====================================================
                // 1. REGRAS PÚBLICAS (Devem vir PRIMEIRO!)
                // ====================================================
                
                // Permite CADASTRO de usuário (POST) sem precisar de token
                .requestMatchers(HttpMethod.POST, "/api/users").permitAll()

                // Permite LOGIN (POST) sem precisar de token (vamos criar isso jaja)
                .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()

                // Permite acessar o Swagger (Documentação) e Health Check
                .requestMatchers(
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/actuator/health"
                ).permitAll()

                .requestMatchers(HttpMethod.GET, "/api/auth/confirm").permitAll()

                // ====================================================
                // 2. REGRAS PRIVADAS (Protegidas)
                // ====================================================
                
                // Qualquer outra rota que comece com /api/ EXIGE um token JWT válido
                // Se o usuário não mandar o token, recebe erro 401
                .requestMatchers("/api/**").authenticated()

                // ====================================================
                // 3. OUTROS
                // ====================================================
                // Permite qualquer outra requisição que não caiu nas regras acima
                .anyRequest().permitAll()
            )
            // Configura o servidor para validar o Token JWT com o Cognito
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));

        return http.build();
    }
}