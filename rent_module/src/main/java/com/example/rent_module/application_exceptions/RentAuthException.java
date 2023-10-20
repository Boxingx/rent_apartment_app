package com.example.rent_module.application_exceptions;

public class RentAuthException extends RuntimeException {
    public RentAuthException(String message) {
        super(message);
    }
}
