package com.rest_au_rant.controller;

import com.rest_au_rant.exception.ResourceNotFoundException;
import com.rest_au_rant.exception.RestaurantException;
import com.rest_au_rant.exception.UnauthorizedException;
import com.rest_au_rant.model.DishCategory;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ControllerExceptionHandler {

    // Handle Foreign Key Constraint Violations
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> handleSQLIntegrityConstraintViolation(SQLIntegrityConstraintViolationException e) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Database constraint violation");
        response.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // Handle RestaurantException
    @ExceptionHandler(RestaurantException.class)
    public ResponseEntity<Map<String, String>> handleRestaurantException(RestaurantException e) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "RestaurantException");
        response.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // Handle ResourceNotFoundException (e.g., Manager Not Found)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleResourceNotFoundException(ResourceNotFoundException e) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "ResourceNotFoundException");
        response.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // Handle UnauthorizedException (e.g., User not allowed to edit Restaurant)
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Map<String, String>> handleUnauthorizedException(UnauthorizedException e) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "UnauthorizedException");
        response.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    // Handle Generic Exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenericException(Exception e) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Internal Server Error");
        response.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleEnumMismatch(MethodArgumentTypeMismatchException ex) {
        if (ex.getRequiredType() == DishCategory.class) {
            return ResponseEntity.badRequest().body("Invalid category. Allowed values: 'STARTER', 'MAIN', 'DESERT', 'HOTDRINK', 'SOFTDRINK', 'ALCOHOL'");
        }
        return ResponseEntity.badRequest().body("Invalid input.");
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> handleConstraintViolation(ConstraintViolationException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Validation Failed");

        // get the first violation message (you can also return all if you want)
        String message = ex.getConstraintViolations()
                .iterator()
                .next()
                .getMessage();

        response.put("message", message);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Handle MethodArgumentNotValidException (e.g., from @Valid on @RequestBody)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
