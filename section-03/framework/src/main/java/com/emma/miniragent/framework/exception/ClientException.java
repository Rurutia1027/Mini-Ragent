package com.emma.miniragent.framework.exception;

/**
 * Client-side / request error (bad input, unauthorized, etc.).
 */
public class ClientException extends RuntimeException {

    private final String code;

    public ClientException(String message) {
        this("A000001", message);
    }

    public ClientException(String code, String message) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
