package com.emma.miniragent.framework.web;

import com.emma.miniragent.framework.convention.Result;
import com.emma.miniragent.framework.exception.ClientException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ClientException.class)
    public Result<Void> handleClientException(HttpServletRequest request, ClientException ex) {
        log.warn("[{}] {} [client] {}", request.getMethod(), request.getRequestURI(), ex.getMessage());
        return Results.failure(ex);
    }

    @ExceptionHandler(Throwable.class)
    public Result<Void> handleThrowable(HttpServletRequest request, Throwable ex) {
        log.error("[{}] {} ", request.getMethod(), request.getRequestURI(), ex);
        return Results.failure();
    }
}
