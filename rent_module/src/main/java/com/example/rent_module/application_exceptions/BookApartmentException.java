package com.example.rent_module.application_exceptions;

import com.example.rent_module.model.dto.ApartmentDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookApartmentException extends RuntimeException {

    private String ERROR_MESSAGE;

    private ApartmentDto targetApartment;


}
