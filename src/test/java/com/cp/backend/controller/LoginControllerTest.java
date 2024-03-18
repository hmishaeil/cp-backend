package com.cp.backend.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Base64;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cp.backend.entity.Auth;
import com.cp.backend.exception.AuthFailedException;
import com.cp.backend.exception.ResourceNotFoundException;
import com.cp.backend.repo.AuthRepo;
import com.cp.backend.request.LoginRequest;
import com.cp.backend.response.LoginResponse;

@ExtendWith(MockitoExtension.class)
class LoginControllerTest {

    @Mock
    private AuthRepo authRepo;

    @InjectMocks
    private LoginController loginController;

    @Test
    void login_WithValidCredentials_ReturnsApiKey() {

        String username = "testUser";
        String password = "testPassword";
        String encodedCredentials = Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
        LoginRequest request = new LoginRequest(username, password);
        Auth auth = new Auth();
        auth.setApiKey(encodedCredentials);

        when(authRepo.findOneByUsername(username)).thenReturn(Optional.of(auth));

        LoginResponse response = loginController.login(request);

        assertNotNull(response);
        assertEquals(encodedCredentials, response.getApiKey());
    }

    @Test
    void login_WithInvalidUsername_ThrowsResourceNotFoundException() {

        String username = "invalidUser";
        String password = "testPassword";
        LoginRequest request = new LoginRequest(username, password);

        when(authRepo.findOneByUsername(username)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> loginController.login(request));
    }

    @Test
    void login_WithInvalidPassword_ThrowsAuthFailedException() {

        String username = "testUser";
        String password = "invalidPassword";
        LoginRequest request = new LoginRequest(username, password);
        Auth auth = new Auth();
        auth.setApiKey(Base64.getEncoder().encodeToString((username + ":" + "correctPassword").getBytes()));

        when(authRepo.findOneByUsername(username)).thenReturn(Optional.of(auth));

        assertThrows(AuthFailedException.class, () -> loginController.login(request));
    }
}
