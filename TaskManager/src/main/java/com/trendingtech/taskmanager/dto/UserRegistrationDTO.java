package com.trendingtech.taskmanager.dto;

import lombok.Data;

@Data
public class UserRegistrationDTO {
    private String email;
    private String password; // Senha (vai para o Cognito, não para o banco)
}