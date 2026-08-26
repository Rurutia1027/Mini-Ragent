package com.emma.miniragent.idm.auth.service;

import com.emma.miniragent.framework.exception.ClientException;
import com.emma.miniragent.idm.auth.dto.LoginResponse;
import com.emma.miniragent.idm.auth.token.TokenStore;
import com.emma.miniragent.idm.user.User;
import com.emma.miniragent.idm.user.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final TokenStore tokenStore;
    private final PasswordEncoder passwordEncoder;
    private final long tokenTtlSeconds;

    public AuthService(UserRepository userRepository,
                       TokenStore tokenStore,
                       PasswordEncoder passwordEncoder,
                       @Value("${auth.token-ttl-seconds:86400}") long tokenTtlSeconds) {
        this.userRepository = userRepository;
        this.tokenStore = tokenStore;
        this.passwordEncoder = passwordEncoder;
        this.tokenTtlSeconds = tokenTtlSeconds;
    }

    public void register(String username, String password) {
        validateCredentials(username, password);
        if (userRepository.existsByUsername(username.trim())) {
            throw new ClientException("Username already exist!");
        }
        userRepository.insert(username.trim(), passwordEncoder.encode(password));
    }

    public LoginResponse login(String username, String password) {
        validateCredentials(username, password);
        User user = userRepository.findByUsername(username.trim())
                .orElseThrow(() -> new ClientException("Username or password issue"));
        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new ClientException("Username or password issue");
        }
        String token = UUID.randomUUID().toString().replace("-", "");
        tokenStore.save(token, user.getUsername(), tokenTtlSeconds);
        return new LoginResponse(token, user.getUsername());
    }

    public void logout(String token) {
        if (token != null && !token.isBlank()) {
            tokenStore.remove(token);
        }
    }

    public String requireUsername(String token) {
        if (token == null || token.isBlank()) {
            throw new ClientException("Not login or login session expired");
        }
        String username = tokenStore.getUsername(token);
        if (username == null) {
            throw new ClientException("Not login or login session expired");
        }
        return username;
    }

    private void validateCredentials(String username, String password) {
        if (username == null || username.isBlank()) {
            throw new ClientException("Username cannot be blank!");
        }
        if (password == null || password.isBlank()) {
            throw new ClientException("Password cannot be blank!");
        }
        if (username.trim().length() < 3) {
            throw new ClientException("User name at least 3 characters");
        }
        if (password.length() < 6) {
            throw new ClientException("Password at least 6 characters");
        }
    }
}
