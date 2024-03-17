package com.cp.backend.exception;

public class AuthFailedException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public AuthFailedException() {
        super("Auth failed!");
    }

    public AuthFailedException(String msg) {
        super(msg);
    }
}
