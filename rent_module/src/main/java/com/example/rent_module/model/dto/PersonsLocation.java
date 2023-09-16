package com.example.rent_module.model.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Pattern;

import static com.example.rent_module.constant_project.ConstantProject.*;


@Getter
@Setter
@Validated
public class PersonsLocation {

    @Pattern(regexp = "^\\d+$", message = LATITUDE_ERROR)
    private String latitude;

    @Pattern(regexp = VALIDATION_LOCATION_PATTERN, message = LONGITUDE_ERROR)
    private String longitude;

}
