package com.emma.miniragent.framework.web;

import com.emma.miniragent.framework.convention.Result;
import com.emma.miniragent.framework.exception.ClientException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ClientException.class)
    public ResponseEntity<Result<Void>> handleClientException(HttpServletRequest request, ClientException ex) {
        log.warn("[{}] {} [client] {}", request.getMethod(), request.getRequestURI(), ex.getMessage());
        HttpStatus status = isUnauthorized(ex) ? HttpStatus.UNAUTHORIZED : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(Results.failure(ex));
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<Result<Void>> handleThrowable(HttpServletRequest request, Throwable ex) {
        log.error("[{}] {} ", request.getMethod(), request.getRequestURI(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Results.failure());
    }

    private static boolean isUnauthorized(ClientException ex) {
        String message = ex.getMessage();
        if (message == null) {
            return false;
        }
        String lower = message.toLowerCase();
        return lower.contains("not login") || lower.contains("login expired");
    }
}
