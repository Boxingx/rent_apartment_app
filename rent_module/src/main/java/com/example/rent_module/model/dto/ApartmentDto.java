package com.example.rent_module.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApartmentDto {

    private String roomsCount;

    private String averageRating;

    private String price;

    private String status;

    private String registrationDate;

    private AddressDto addressDto;
}
