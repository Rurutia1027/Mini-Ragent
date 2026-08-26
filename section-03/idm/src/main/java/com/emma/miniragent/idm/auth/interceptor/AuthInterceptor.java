package com.emma.miniragent.idm.auth.interceptor;

import com.emma.miniragent.idm.auth.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Requires {@code Authorization: Bearer <token>} for protected paths.
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {

    public static final String ATTR_USERNAME = "auth.username";

    private final AuthService authService;

    public AuthInterceptor(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String authorization = request.getHeader("Authorization");
        String token = extractBearer(authorization);
        String username = authService.requireUsername(token);
        request.setAttribute(ATTR_USERNAME, username);
        return true;
    }

    private static String extractBearer(String authorization) {
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return null;
        }
        return authorization.substring("Bearer ".length()).trim();
    }
}
