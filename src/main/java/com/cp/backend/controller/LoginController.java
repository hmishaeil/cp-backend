package com.cp.backend.controller;

import java.util.Base64;
import java.util.Optional;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cp.backend.entity.Auth;
import com.cp.backend.exception.AuthFailedException;
import com.cp.backend.exception.ResourceNotFoundException;
import com.cp.backend.repo.AuthRepo;
import com.cp.backend.request.LoginRequest;
import com.cp.backend.response.LoginResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/login")
public class LoginController {

    private final AuthRepo authRepo;

    @PostMapping
    public LoginResponse login(@RequestBody @Valid LoginRequest request) {

        Optional<Auth> optionalAuthEntity = authRepo.findOneByUsername(request.getUsername());
        if (!optionalAuthEntity.isPresent()) {
            throw new ResourceNotFoundException();
        } else if (!optionalAuthEntity.get().getApiKey().equals(Base64.getEncoder()
                .encodeToString(
                        String.format("%s:%s", request.getUsername(), request.getPassword())
                                .getBytes()))) {
            throw new AuthFailedException();
        }

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setApiKey(optionalAuthEntity.get().getApiKey());

        return loginResponse;

    }

}
