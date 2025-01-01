package com.virgo.dynamic_form.utils.responseWrapper;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Map;

public class Response {
    public static ResponseEntity<ErrorResponse> error(String message, Map<String, String[]> errors, HttpStatus status) {
        return ResponseEntity.status(status).body(
                ErrorResponse.builder()
                        .message(message)
                        .errors(errors)
                        .build()
        );
    }

    public static ResponseEntity<ErrorResponse> error(String message, HttpStatus status) {
        return ResponseEntity.status(status).body(
                ErrorResponse.builder()
                        .message(message)
                        .build()
        );
    }
}