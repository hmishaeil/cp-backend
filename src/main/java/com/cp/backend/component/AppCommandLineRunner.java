package com.cp.backend.component;

import java.util.Base64;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.cp.backend.entity.Auth;
import com.cp.backend.repo.AuthRepo;
import com.cp.backend.service.ApiKeyCache;

@Component
public class AppCommandLineRunner implements CommandLineRunner {

    private PasswordEncoder passwordEncoder;
    private ApiKeyCache apiKeyCache;

    @Value("${app.username}")
    private String username;

    @Value("${app.password}")
    private String password;

    private final AuthRepo authRepo;

    public AppCommandLineRunner(AuthRepo authRepo, PasswordEncoder passwordEncoder, ApiKeyCache apiKeyCache) {
        this.authRepo = authRepo;
        this.passwordEncoder = passwordEncoder;
        this.apiKeyCache = apiKeyCache;
    }

    @Override
    public void run(String... args) throws Exception {

        Optional<Auth> optionalAuth = authRepo.findOneByUsername(username);

        Auth auth;
        if (optionalAuth.isEmpty()) {
            auth = new Auth();
            auth.setUsername(username);
            auth.setPassword(passwordEncoder.encode(password));
            auth.setApiKey(Base64.getEncoder()
                    .encodeToString(
                            String.format("%s:%s", username, password)
                                    .getBytes()));
            authRepo.save(auth);
        } else {
            auth = optionalAuth.get();
        }

        apiKeyCache.set(auth.getApiKey());

    }
}
