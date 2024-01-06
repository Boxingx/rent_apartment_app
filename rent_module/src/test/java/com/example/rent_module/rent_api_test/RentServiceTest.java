package com.example.rent_module.rent_api_test;

import com.example.rent_module.model.dto.ApartmentWithMessageDto;
import com.example.rent_module.model.dto.GetAddressInfoResponseDto;
import com.example.rent_module.model.entity.AddressEntity;
import com.example.rent_module.model.entity.ApartmentEntity;
import com.example.rent_module.repository.ApartmentRepository;
import com.example.rent_module.service.RentApartmentService;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.rent_module.constant_project.ConstantProject.*;
import static org.mockito.ArgumentMatchers.anyString;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class RentServiceTest {

    @Autowired
    private RentApartmentService rentApartmentService;

    @Mock
    ApartmentRepository apartmentRepository;



    /**
     * Тест метода сервиса getApartmentById.
     * */
    @Test
    public void getApartmentByIdTest() throws Exception {
        ApartmentWithMessageDto apartmentById = rentApartmentService.getApartmentById(1l);
        Assertions.assertNotNull(apartmentById);
        Assertions.assertEquals("5000", apartmentById.getApartmentDto().getPrice());
    }


    /**
     * Тест метода сервиса bookApartment
     * */
    //todo test как можно тестировать этот метод если я не знаю в каком статусе квартира на данный момент?
    @Test
    public void bookApartmentTest() {

        ApartmentWithMessageDto apartmentWithMessageDto = rentApartmentService.bookApartment(1l,
                LocalDate.of(2023, 9, 15),
                LocalDate.of(2023, 9, 21),
                "slf4g",
                "testToken|2030-10-07T12:49:43.641604600");

        Assertions.assertNotNull(apartmentWithMessageDto);
        Assertions.assertNotNull(apartmentWithMessageDto.getApartmentDto());

    }


    //todo
    @Test
    public void getApartmentByCityAndPriceAndRoomsCountTest(){

        ApartmentEntity apt1 = new ApartmentEntity();
        AddressEntity address1 = new AddressEntity();
        address1.setCity("Омск");
        apt1.setRoomsCount("3");
        apt1.setPrice("1400");
        apt1.setAddressEntity(address1);

        ApartmentEntity apt2 = new ApartmentEntity();
        AddressEntity address2 = new AddressEntity();
        address1.setCity("Омск");
        apt2.setRoomsCount("3");
        apt2.setPrice("1700");
        apt2.setAddressEntity(address2);

        ApartmentEntity apt3 = new ApartmentEntity();
        AddressEntity address3 = new AddressEntity();
        address1.setCity("Омск");
        apt3.setRoomsCount("3");
        apt3.setPrice("1200");
        apt3.setAddressEntity(address3);

        List<ApartmentEntity> list = new ArrayList<>();
        list.add(apt1);
        list.add(apt2);
        list.add(apt3);

        Mockito.when(apartmentRepository.findApartmentEntitiesByRoomsCountAndPriceToAndAddressEntity_City(anyString(), anyString(), anyString()))
                .thenReturn(list);

        GetAddressInfoResponseDto getAddressInfoResponseDto = rentApartmentService.getApartmentByCityAndPriceAndRoomsCount("3","1800","Омск");

        Assertions.assertNotNull(getAddressInfoResponseDto);
        Assertions.assertEquals(3, getAddressInfoResponseDto.getApartmentDtoList().size());

    }

//    @Override
//    public GetAddressInfoResponseDto getApartmentByCityAndPriceAndRoomsCount(String city, String priceTo, String roomsCount) {
//        List<ApartmentEntity> apartmentEntitiesList = apartmentRepository.findApartmentEntitiesByRoomsCountAndPriceToAndAddressEntity_City(roomsCount, priceTo, city);
//        if (apartmentEntitiesList.isEmpty()) {
//            GetAddressInfoResponseDto getAddressInfoResponseDto = new GetAddressInfoResponseDto(null);
//            getAddressInfoResponseDto.setExceptionCode(ERROR_CODE_255);
//            getAddressInfoResponseDto.setExceptionMessage(NO_APARTMENT_IN_CITY_AND + city + WITH_PRICE + priceTo + AND_ROOMS_COUNT + roomsCount);
//
//            return getAddressInfoResponseDto;
//        }
//        return new GetAddressInfoResponseDto(apartmentEntityToDto(apartmentEntitiesList));
//    }
}
