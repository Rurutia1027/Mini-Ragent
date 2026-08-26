package com.emma.miniragent.idm.auth.controller;

import com.emma.miniragent.framework.convention.Result;
import com.emma.miniragent.framework.web.Results;
import com.emma.miniragent.idm.auth.dto.LoginRequest;
import com.emma.miniragent.idm.auth.dto.LoginResponse;
import com.emma.miniragent.idm.auth.dto.RegisterRequest;
import com.emma.miniragent.idm.auth.service.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public Result<Void> register(@RequestBody RegisterRequest request) {
        authService.register(request.getUsername(), request.getPassword());
        return Results.success();
    }

    @PostMapping("/login")
    public Result<LoginResponse> login(@RequestBody LoginRequest request) {
        return Results.success(authService.login(request.getUsername(), request.getPassword()));
    }

    @PostMapping("/logout")
    public Result<Void> logout(@RequestHeader(value = "Authorization", required = false) String authorization) {
        authService.logout(extractBearer(authorization));
        return Results.success();
    }

    private static String extractBearer(String authorization) {
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return null;
        }
        return authorization.substring("Bearer ".length()).trim();
    }
}
