package com.emma.miniragent.framework.web;

import com.emma.miniragent.framework.convention.Result;
import com.emma.miniragent.framework.exception.ClientException;

/**
 * Helpers for building {@link Result} responses.
 */
public final class Results {

    private Results() {
    }

    public static Result<Void> success() {
        return new Result<Void>().setCode(Result.SUCCESS_CODE);
    }

    public static <T> Result<T> success(T data) {
        return new Result<T>().setCode(Result.SUCCESS_CODE).setData(data);
    }

    public static Result<Void> failure(String code, String message) {
        return new Result<Void>().setCode(code).setMessage(message);
    }

    public static Result<Void> failure(ClientException ex) {
        return failure(ex.getCode(), ex.getMessage());
    }

    public static Result<Void> failure() {
        return failure("B000001", "系统执行出错");
    }
}
