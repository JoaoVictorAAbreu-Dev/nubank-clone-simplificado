package com.taskflowdev.nubankclone.auth;

import com.taskflowdev.nubankclone.auth.dto.LoginRequest;
import com.taskflowdev.nubankclone.auth.dto.RegisterRequest;
import com.taskflowdev.nubankclone.security.JwtService;
import com.taskflowdev.nubankclone.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class AuthServiceTests {

    @Autowired
    private AuthService authService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private JwtService jwtService;

    @Test
    void registerGeneratesToken() {
        when(userRepository.existsByEmail("user@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encoded");
        when(jwtService.generateToken("user@example.com")).thenReturn("token");

        var response = authService.register(new RegisterRequest("user@example.com", "password123"));

        assertNotNull(response.token());
    }

    @Test
    void loginGeneratesToken() {
        var user = new com.taskflowdev.nubankclone.user.UserAccount("user@example.com", "encoded");
        when(userRepository.findByEmail("user@example.com")).thenReturn(java.util.Optional.of(user));
        when(passwordEncoder.matches("password123", "encoded")).thenReturn(true);
        when(jwtService.generateToken("user@example.com")).thenReturn("token");

        var response = authService.login(new LoginRequest("user@example.com", "password123"));

        assertNotNull(response.token());
    }
}
