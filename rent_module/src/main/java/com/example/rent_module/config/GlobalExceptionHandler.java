package com.example.rent_module.config;

import com.example.rent_module.application_exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BindException.class)
    public ResponseEntity<List<ValidationErrorDetails>> handleBindException(BindException ex) {
        List<ValidationErrorDetails> errorDetailsList = new ArrayList<>();
        for (FieldError error : ex.getFieldErrors()) {
            ValidationErrorDetails errorDetails = new ValidationErrorDetails(error.getField(), error.getDefaultMessage());
            errorDetailsList.add(errorDetails);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetailsList);
    }

    @ExceptionHandler(IntegrationConfigurationException.class)
    public ResponseEntity<?> handleBindException(IntegrationConfigurationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(ApartmentException.class)
    public ResponseEntity<?> handleBindException(ApartmentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(BookApartmentException.class)
    public ResponseEntity<?> handleBindException(BookApartmentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getERROR_MESSAGE() + "\n" + ex.getTargetApartment().toString());
    }

    @ExceptionHandler(RentAuthException.class)
    public ResponseEntity<?> handleBindException(RentAuthException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(RentRegistrationException.class)
    public ResponseEntity<?> handleBindException(RentRegistrationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<?> handleBindException(NumberFormatException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(RatingException.class)
    public ResponseEntity<?> handleBindException(RatingException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

}
