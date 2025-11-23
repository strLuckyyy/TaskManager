package com.trendingtech.taskmanager.service;

import com.trendingtech.taskmanager.dto.UserRegistrationDTO;
import com.trendingtech.taskmanager.dto.LoginRequestDTO;
import com.trendingtech.taskmanager.dto.LoginResponseDTO;
import com.trendingtech.taskmanager.dto.UserResponseDTO;
import com.trendingtech.taskmanager.model.User;
import com.trendingtech.taskmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AttributeType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AuthFlowType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.InitiateAuthRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.InitiateAuthResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.SignUpRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.SignUpResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ConfirmSignUpRequest;
import java.util.Map;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    
    // Cliente da AWS (Injetado via AwsConfig)
    private final CognitoIdentityProviderClient cognitoClient;

    // Lê o Client ID do application.properties
    @Value("${aws.cognito.client-id}")
    private String clientId;

    public User createUser(UserRegistrationDTO registrationDTO) {
        
        // 1. Tenta criar o usuário lá na AWS (Cognito)
        // Se a senha for fraca ou email existir, a AWS lança exceção aqui e o código para.
        SignUpRequest signUpRequest = SignUpRequest.builder()
                .clientId(clientId)
                .username(registrationDTO.getEmail()) // O username no Cognito será o email
                .password(registrationDTO.getPassword())
                .userAttributes(
                        AttributeType.builder().name("email").value(registrationDTO.getEmail()).build()
                )
                .build();

        SignUpResponse response = cognitoClient.signUp(signUpRequest);

        // 2. Se a AWS aceitou, salvamos o perfil no nosso banco (SEM A SENHA)
        User newUser = new User();
        newUser.setEmail(registrationDTO.getEmail());
        
        // Salvamos o ID que a AWS gerou (sub) para vincular os dados
        newUser.setCognitoId(response.userSub()); 

        return userRepository.save(newUser);
    }

    public void confirmUser(String email, String confirmationCode) {
        ConfirmSignUpRequest confirmSignUpRequest = ConfirmSignUpRequest.builder()
                .clientId(clientId)
                .username(email)
                .confirmationCode(confirmationCode)
                .build();

        cognitoClient.confirmSignUp(confirmSignUpRequest);
    }

    public LoginResponseDTO login(LoginRequestDTO loginRequest) {
        
        InitiateAuthRequest authRequest = InitiateAuthRequest.builder()
                .clientId(clientId)
                .authFlow(AuthFlowType.USER_PASSWORD_AUTH)
                .authParameters(Map.of(
                        "USERNAME", loginRequest.getEmail(),
                        "PASSWORD", loginRequest.getPassword()
                ))
                .build();

        InitiateAuthResponse response = cognitoClient.initiateAuth(authRequest);
        String token = response.authenticationResult().idToken();

        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("User found in Cognito but not in Database!"));

        // 3. Monta a resposta completa
        LoginResponseDTO loginResponse = new LoginResponseDTO();
        loginResponse.setToken(token);
        loginResponse.setUser(new UserResponseDTO(user)); // Converte para DTO

        return loginResponse;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}