package com.taskflowdev.nubankclone.auth;

import com.taskflowdev.nubankclone.account.Account;
import com.taskflowdev.nubankclone.account.AccountRepository;
import com.taskflowdev.nubankclone.auth.dto.LoginRequest;
import com.taskflowdev.nubankclone.auth.dto.RegisterRequest;
import com.taskflowdev.nubankclone.config.JwtProperties;
import com.taskflowdev.nubankclone.security.JwtService;
import com.taskflowdev.nubankclone.user.UserAccount;
import com.taskflowdev.nubankclone.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private AuthService authService;

    @BeforeEach
    void setUp() {
        JwtProperties jwtProperties = new JwtProperties("01234567890123456789012345678901", 60);
        JwtService jwtService = new JwtService(jwtProperties);
        authService = new AuthService(userRepository, accountRepository, passwordEncoder, jwtService);
    }

    @Test
    void registerGeneratesToken() {
        when(userRepository.existsByEmail("user@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encoded");
        when(userRepository.save(any(UserAccount.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var response = authService.register(new RegisterRequest("user@example.com", "password123"));

        assertNotNull(response.token());
    }

    @Test
    void loginGeneratesToken() {
        var user = new UserAccount("user@example.com", "encoded");
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password123", "encoded")).thenReturn(true);

        var response = authService.login(new LoginRequest("user@example.com", "password123"));

        assertNotNull(response.token());
    }
}
