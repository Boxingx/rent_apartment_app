package com.example.rent_module.application_exceptions;

import lombok.Getter;

@Getter
public class ApartmentException extends RuntimeException {

    public ApartmentException(String message) {
        super(message);
    }
}
