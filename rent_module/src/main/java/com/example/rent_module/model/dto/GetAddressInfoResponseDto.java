package com.example.rent_module.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetAddressInfoResponseDto extends WeatherOnRentPoint {

    private List<ApartmentDto> apartmentDtoList;

}
