package com.trendingtech.taskmanager.dto;

import lombok.Data;

@Data
public class LoginResponseDTO {
    private String token;
    private UserResponseDTO user;
}