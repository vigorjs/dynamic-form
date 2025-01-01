package com.virgo.dynamic_form.config.advisers;

import com.virgo.dynamic_form.config.advisers.exception.*;
import com.virgo.dynamic_form.utils.responseWrapper.Response;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class AppWideExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String[]> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            String fieldName = error.getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, new String[]{errorMessage});
        });
        return Response.error("Invalid field", errors, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintValidationExceptions(ConstraintViolationException ex) {
        Map<String, String[]> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(violation -> {
            String fieldName = violation.getPropertyPath().toString();
            String errorMessage = violation.getMessage();
            errors.put(fieldName, new String[]{errorMessage});
        });
        return Response.error("Invalid field", errors, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler({BadCredentialsException.class, AuthenticationException.class})
    public ResponseEntity<?> handleAuthenticationException(Exception ex) {
        return Response.error("Email or password incorrect", HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(DuplicateEntryException.class)
    public ResponseEntity<?> handleDuplicateEntryException(DuplicateEntryException ex) {
        Map<String, String[]> errors = new HashMap<>();
        String fieldName = ex.getMessage().toLowerCase().contains("slug") ? "slug" : "email";
        errors.put(fieldName, new String[]{ex.getMessage()});
        return Response.error("Invalid field", errors, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<?> handleInvalidRequestException(InvalidRequestException ex) {
//        Map<String, String[]> errors = new HashMap<>();
//        errors.put("answers", new String[]{ex.getMessage()});
        return Response.error(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleNotFoundException(NotFoundException ex) {
        return Response.error(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ForbiddenAccessException.class)
    public ResponseEntity<?> handleForbiddenException(ForbiddenAccessException ex) {
        return Response.error("Forbidden access", HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<?> handleInvalidTokenException(InvalidTokenException ex) {
        return Response.error("Unauthenticated.", HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGlobalException(Exception ex) {
        log.error("Unexpected error occurred", ex);
        return Response.error("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}