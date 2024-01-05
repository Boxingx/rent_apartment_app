package com.example.product_module.application_exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ProductExceptionHandler {

    @ExceptionHandler(BookingHistoryException.class)
    public ResponseEntity<?> handleBindException(BookingHistoryException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getBOOKING_EXCEPTION_MESSAGE());
    }

}
