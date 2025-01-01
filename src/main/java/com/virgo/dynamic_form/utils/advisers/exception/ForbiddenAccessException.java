package com.virgo.dynamic_form.utils.advisers.exception;

public class ForbiddenAccessException extends RuntimeException {
    public ForbiddenAccessException(String message) {
        super(message);
    }

    public ForbiddenAccessException() {
        super("You are not allowed to access this information details");
    }
}
