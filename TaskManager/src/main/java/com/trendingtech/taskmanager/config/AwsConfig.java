package com.trendingtech.taskmanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;

@Configuration
public class AwsConfig {

    @Bean
    public CognitoIdentityProviderClient cognitoIdentityProviderClient() {
        // Ajuste a região se você criou em outra (ex: US_EAST_1)
        return CognitoIdentityProviderClient.builder()
                .region(Region.US_EAST_2) 
                .build();
    }
}