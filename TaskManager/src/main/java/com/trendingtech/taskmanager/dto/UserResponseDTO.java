package com.trendingtech.taskmanager.dto;

import com.trendingtech.taskmanager.model.User;
import lombok.Data;

@Data
public class UserResponseDTO {
    private Long id;
    private String username;
    private String email;

    // Construtor utilitário para converter Entity -> DTO
    public UserResponseDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
    }
}