package com.example.rent_module.application_exceptions;

import lombok.Getter;

@Getter
public class IntegrationConfigurationException extends RuntimeException {

    private final String ERROR_DESCRIPTION = "Отсутствует конфигурация для текущей интеграции";

}
