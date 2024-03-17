package com.cp.backend.exception;

public class DuplicateResourceException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public DuplicateResourceException() {
        super("Duplicate resource found!");
    }

    public DuplicateResourceException(String msg) {
        super(msg);
    }
}
