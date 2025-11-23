package com.trendingtech.taskmanager.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import software.amazon.awssdk.services.cognitoidentityprovider.model.CognitoIdentityProviderException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Captura qualquer erro que venha do AWS Cognito (Senha fraca, Email duplicado, etc)
    @ExceptionHandler(CognitoIdentityProviderException.class)
    public ResponseEntity<String> handleCognitoException(CognitoIdentityProviderException ex) {
        // Pega a mensagem de erro amigável da AWS
        String errorMessage = ex.awsErrorDetails().errorMessage();
        
        // Retorna erro 400 (Bad Request) com o texto do erro
        return ResponseEntity.badRequest().body(errorMessage);
    }

    // Captura outros erros genéricos
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        return ResponseEntity.internalServerError().body("An unexpected error occurred: " + ex.getMessage());
    }
}