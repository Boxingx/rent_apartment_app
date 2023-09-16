package com.example.rent_module.application_exceptions;

import lombok.Getter;

@Getter
public class ApartmentException extends RuntimeException {

    private final String APARTMENT_ERROR = "123";

}
