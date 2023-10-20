package com.example.rent_module.application_exceptions;

import lombok.Getter;

@Getter
public class IntegrationConfigurationException extends RuntimeException {

    public IntegrationConfigurationException(String message){
        super(message);
    }

}
