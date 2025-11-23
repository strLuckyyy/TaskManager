package com.trendingtech.taskmanager.controller;

import com.trendingtech.taskmanager.dto.LoginRequestDTO;
import com.trendingtech.taskmanager.dto.LoginResponseDTO;
import com.trendingtech.taskmanager.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth") // <--- OLHA O PREFIXO AQUI
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/login") // <--- E O FINAL AQUI (/api/auth/login)
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequest) {
        // O Service já retorna o DTO completo agora
        LoginResponseDTO response = userService.login(loginRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/confirm")
    public ResponseEntity<String> confirmAccount(@RequestParam String email, @RequestParam String code) {
        try {
            userService.confirmUser(email, code);
            
            // Retorna uma página HTML simples
            String htmlSuccess = """
                <html>
                    <body style='font-family: Arial; text-align: center; padding-top: 50px;'>
                        <h1 style='color: green;'>Conta Confirmada! 🎉</h1>
                        <p>Seu cadastro foi validado com sucesso.</p>
                        <p>Você já pode fechar esta janela e fazer login no aplicativo.</p>
                    </body>
                </html>
            """;
            return ResponseEntity.ok().body(htmlSuccess);
            
        } catch (Exception e) {
            String htmlError = """
                <html>
                    <body style='font-family: Arial; text-align: center; padding-top: 50px;'>
                        <h1 style='color: red;'>Erro ao Confirmar 😢</h1>
                        <p>O código pode ter expirado ou o e-mail está incorreto.</p>
                        <p>Erro: %s</p>
                    </body>
                </html>
            """.formatted(e.getMessage()); // Mostra o erro simplificado
            return ResponseEntity.badRequest().body(htmlError);
        }
    }
}