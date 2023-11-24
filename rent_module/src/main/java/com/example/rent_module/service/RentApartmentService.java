package com.example.rent_module.service;


import com.example.rent_module.model.dto.ApartmentDto;
import com.example.rent_module.model.dto.ApartmentWithMessageDto;
import com.example.rent_module.model.dto.GetAddressInfoResponseDto;
import com.example.rent_module.model.dto.PersonsLocation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

public interface RentApartmentService {

    GetAddressInfoResponseDto getAddressByCity(String cityName);

    GetAddressInfoResponseDto getApartmentByPrice(Long price);

    GetAddressInfoResponseDto getApartmentByCityAndRoomsCount(String city, String roomsCount);

    GetAddressInfoResponseDto getApartmentByCityAndPrice(String city, String price);

    GetAddressInfoResponseDto getApartmentByCityAndPriceAndRoomsCount(String city, String priceTo, String roomsCount);

    GetAddressInfoResponseDto findApartmentEntitiesByAverageRatingAndAddressEntity_City(String cityName, String averageRating);

    GetAddressInfoResponseDto getApartmentsByLocation(PersonsLocation location);

    ApartmentWithMessageDto getApartmentById(Long id);

    ApartmentWithMessageDto registrationNewApartment(ApartmentDto apartmentDto, String token);

    ApartmentWithMessageDto bookApartment(Long id, LocalDate start, LocalDate end, String promoCode, String userToken);

    ResponseEntity<byte[]> getImage(Long id);

    String addPhoto(Long id, MultipartFile multipartFile);

    GetAddressInfoResponseDto getApartmentByCityAndPriceFromTo(String cityName, String priceFrom, String priceTo);

    String addReviewForApartment(Integer rating, String review, Long apartmentId);

}

