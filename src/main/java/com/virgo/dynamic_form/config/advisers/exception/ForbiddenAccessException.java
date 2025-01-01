package com.virgo.dynamic_form.config.advisers.exception;

public class ForbiddenAccessException extends RuntimeException {
    public ForbiddenAccessException(String message) {
        super(message);
    }

    public ForbiddenAccessException() {
        super("Forbidden access");
    }
}
