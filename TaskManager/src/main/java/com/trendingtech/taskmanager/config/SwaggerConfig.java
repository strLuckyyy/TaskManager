package com.trendingtech.taskmanager.config;

import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI taskManagerOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                    .title("Task Manager API")
                    .description("API for managing tasks and users")
                    .version("1.0.0")
                        .contact(new Contact()
                                .name("Your Name")
                                .email("your.email@exemple.com"))
                );
    }
}
