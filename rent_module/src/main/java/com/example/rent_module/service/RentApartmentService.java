package com.example.rent_module.service;



import com.example.rent_module.model.dto.ApartmentDto;
import com.example.rent_module.model.dto.ApartmentWithMessageDto;
import com.example.rent_module.model.dto.GetAddressInfoResponseDto;
import com.example.rent_module.model.dto.PersonsLocation;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface RentApartmentService {

    GetAddressInfoResponseDto getAddressByCity(String cityName);

    GetAddressInfoResponseDto getApartmentByPrice(Long price);

    GetAddressInfoResponseDto getApartmentByCityAndRoomsCount(String city, String roomsCount);

    GetAddressInfoResponseDto getApartmentByCityAndPrice(String city, String price);

    GetAddressInfoResponseDto getApartmentByCityAndPriceAndRoomsCount(String city, String price, String roomsCount);

    GetAddressInfoResponseDto findApartmentEntitiesByAverageRatingAndAddressEntity_City(String cityName, String averageRating);

    GetAddressInfoResponseDto getApartmentsByLocation(PersonsLocation location);

    ApartmentWithMessageDto getApartmentById(Long id);

    ApartmentWithMessageDto registrationNewApartment(ApartmentDto apartmentDto);

    ApartmentWithMessageDto bookApartment(Long id, LocalDate start, LocalDate end, String promoCode);

}

