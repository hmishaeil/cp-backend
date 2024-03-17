package com.cp.backend.exception;

public class BadRequestException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public BadRequestException() {
        super("Bad request!");
    }

    public BadRequestException(String msg) {
        super(msg);
    }
}
