package com.example.rent_module.rent_api_test;

import com.example.rent_module.model.dto.ApartmentWithMessageDto;
import com.example.rent_module.service.RentApartmentService;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class RentServiceTest {

    @Autowired
    private RentApartmentService rentApartmentService;


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
    //todo как можно тестировать этот метод если я не знаю в каком статусе квартира на данный момент?
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
}
