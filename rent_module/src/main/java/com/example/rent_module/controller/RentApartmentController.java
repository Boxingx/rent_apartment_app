package com.example.rent_module.controller;


import com.example.rent_module.model.dto.ApartmentDto;
import com.example.rent_module.model.dto.ApartmentWithMessageDto;
import com.example.rent_module.model.dto.GetAddressInfoResponseDto;
import com.example.rent_module.model.dto.PersonsLocation;
import com.example.rent_module.service.AuthService;
import com.example.rent_module.service.RentApartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;

import static com.example.rent_module.constant_project.ConstantProject.*;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@RestController
public class RentApartmentController {

    @Autowired
    private RentApartmentService rentApartmentService;

    @Autowired
    private AuthService authService;


    /**
     * Метод позволяет получить фото по id
     */
    @GetMapping("/api/photo")
    public ResponseEntity<byte[]> getImageById(@RequestParam Long id) {
        return rentApartmentService.getImage(id);
    }

    /**
     * Метод добавления фотографий к квартире
     */
    @PostMapping("/api/add_photo")
    public String addPhotoToApartment(@RequestParam Long id, @RequestParam MultipartFile multipartFile) {
        return rentApartmentService.addPhoto(id, multipartFile);
    }

    /**
     * Метод выгружает пользователю квартиры по определенным параметрам
     */
    @GetMapping(GET_APARTMENT_INFO)
    public GetAddressInfoResponseDto getAddressInfo(@RequestParam String cityName,
                                                    @RequestParam(required = false) String priceFrom,
                                                    @RequestParam(required = false) String priceTo,
                                                    @RequestParam(required = false) String roomsCount,
                                                    @RequestParam(required = false) String averageRating) {
        if (priceFrom == null && priceTo == null && roomsCount == null && averageRating == null) {
            return rentApartmentService.getAddressByCity(cityName);
        }
        if (priceFrom == null && priceTo == null && roomsCount != null && averageRating == null) {
            return rentApartmentService.getApartmentByCityAndRoomsCount(cityName, roomsCount);
        }
        if (priceFrom != null && priceTo != null && roomsCount == null && averageRating == null) {
            return rentApartmentService.getApartmentByCityAndPriceFromTo(cityName, priceFrom, priceTo);
        }
        if (priceTo != null && roomsCount != null && averageRating == null) {
            return rentApartmentService.getApartmentByCityAndPriceAndRoomsCount(cityName, priceTo, roomsCount);
        }
        if (priceFrom == null && priceTo == null &&  roomsCount == null && averageRating != null) {
            return rentApartmentService.findApartmentEntitiesByAverageRatingAndAddressEntity_City(cityName, averageRating);
        }
        return new GetAddressInfoResponseDto(new ArrayList<>());
    }

    /**
     * Метод выгружает квартиры по выбранной локации, определяется населенный пункт и по нему выгружаются квартиры
     */
    @PostMapping(GET_APARTMENT_BY_LOCATION)
    public GetAddressInfoResponseDto getApartmentsByLocation(@Valid @RequestBody PersonsLocation location) {
        return rentApartmentService.getApartmentsByLocation(location);
    }

    /**
     * Метод позволяет просто посмотреть квартиру по id, либо забронировать ее указав id и дату старта и окончания бронирования
     */
    @GetMapping(GET_APARTMENT_BY_ID)
    public ApartmentWithMessageDto getApartmentById(@RequestHeader String token,
                                                    @RequestParam Long id,
                                                    @RequestParam(required = false) LocalDate start,
                                                    @RequestParam(required = false) LocalDate end,
                                                    @RequestParam(required = false) String promoCode) {
        if (isNull(authService.checkValidToken(token))) {
            return new ApartmentWithMessageDto(SIGN_IN, null);
        }

        if (isNull(start) && isNull(end)) {
            return rentApartmentService.getApartmentById(id);
        }
        if (nonNull(start) && nonNull(end)) {
            return rentApartmentService.bookApartment(id, start, end, promoCode, token);
        } else return new ApartmentWithMessageDto();
    }


    //TODO наверное также в этом методе нужно сохранять владельца, т.е тот кто квартиру регистрирует, помимо этого нет адреса, нужно разобраться как передавать адрес.

    /**
     * Метод позволяет добавить новые квартиры
     */
    @PostMapping(ADD_NEW_APARTMENT)
    public ApartmentWithMessageDto addNewApartment(@RequestHeader String token,
                                                   @RequestBody ApartmentDto apartmentDto) {

        if (isNull(authService.checkValidToken(token))) {
            return new ApartmentWithMessageDto(SIGN_IN, null);
        }

        return rentApartmentService.registrationNewApartment(apartmentDto, token);
    }
}
