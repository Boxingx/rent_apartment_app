package com.example.rent_module.rent_api_test;

import com.example.rent_module.application_exceptions.ApartmentException;
import com.example.rent_module.integration.GeoCoderRestTemplateManager;
import com.example.rent_module.integration.YandexWeatherRestTemplateManager;
import com.example.rent_module.model.dto.ApartmentWithMessageDto;
import com.example.rent_module.model.dto.PersonsLocation;
import com.example.rent_module.model.entity.ApartmentEntity;
import com.example.rent_module.service.RentApartmentService;
import com.example.rent_module.service.RentApartmentServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import static com.example.rent_module.constant_project.ConstantProject.*;
import static com.example.rent_module.rent_api_test.PrepareObjectToTest.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class RentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GeoCoderRestTemplateManager restTemplateManager;

    @MockBean
    private YandexWeatherRestTemplateManager yandexWeatherRestTemplateManager;

    @Autowired
    private RentApartmentService rentApartmentService;


//    @Override
//    public ApartmentWithMessageDto getApartmentById(Long id) {
//        ApartmentEntity apartmentEntity = apartmentRepository.findById(id).orElseThrow(() -> new ApartmentException(APARTMENT_ERROR));
//        if (apartmentEntity.getStatus().equals("false")) {
//            return new ApartmentWithMessageDto(APARTMENT_STATUS_FALSE, applicationMapper.apartmentEntityToApartmentDto(apartmentEntity));
//        }
//        return new ApartmentWithMessageDto(APARTMENT_STATUS_TRUE, applicationMapper.apartmentEntityToApartmentDto(apartmentEntity));
//    }

    @Test
    public void getApartmentByIdTest() throws Exception {
        ApartmentWithMessageDto apartmentById = rentApartmentService.getApartmentById(1l);
        Assertions.assertNotNull(apartmentById);
        Assertions.assertEquals("5000", apartmentById.getApartmentDto().getPrice());
    }


    /**
     * Тест метода контроллера getApartmentsByLocation который выгружает апартаменты по локации
     */
    @Test
    public void getApartmentsByLocationTest() throws Exception {

        Mockito.when(restTemplateManager.getInfoByLocation(Mockito.any(PersonsLocation.class))).thenReturn("Omsk");

        Mockito.when(yandexWeatherRestTemplateManager.getWeatherByLocation(Mockito.any(PersonsLocation.class))).thenReturn(prepareYandexWeatherResponseForTest());

        mockMvc.perform(post(GET_APARTMENT_BY_LOCATION)
                        .content(asJSONstring(preparePersonLocationForTest()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.apartmentDtoList").isNotEmpty())
                .andExpect(jsonPath("$.condition")
                        .value("clear"))
                .andExpect(status().is(200));
    }


    /**
     * Тест метода контроллера getApartmentById, передается id который есть в БД и правильный токен.
     * */
    @Test
    public void findApartmentByParamTest() throws Exception {
        mockMvc.perform(get(GET_APARTMENT_BY_ID)
                        .param("id", "5")
                        .header("token", "testToken|2030-10-07T12:49:43.641604600"))
                .andExpect(jsonPath("$.message")
                        .value("Квартира доступна, начать бронирование?"))
                .andExpect(jsonPath("$.apartmentDto").isNotEmpty())
                .andExpect(status().is(200));
    }


    //TODO не работает, тк бросает исключение, как быть?
    /**
     * Тест метода контроллера getApartmentById, передается id которого нет в БД и правильный токен
     * */
    @Test
    public void findApartmentByIdUnknownTest() throws Exception {
        mockMvc.perform(get(GET_APARTMENT_BY_ID)
                        .param("id", "20")
                        .header("token", "testToken|2030-10-07T12:49:43.641604600"))
                .andExpect(status().is(400))
                .andExpect(content().string("Апартаменты не найдены"));
    }


    /**
     * Негативный тест метода AuthService -> checkValidToken, передается неправильный токен.
     * */
    @Test
    public void checkNonValidToken() throws Exception {
        mockMvc.perform(get(GET_APARTMENT_BY_ID)
                        .param("id", "5")
                        .header("token", "testToken2030-10-07T12:49:43.641604600"))
                .andExpect(jsonPath("$.message")
                        .value(SIGN_IN))
                .andExpect(jsonPath("$.apartmentDto").isEmpty())
                .andExpect(status().is(200));
    }


//    @GetMapping(GET_APARTMENT_BY_ID)
//    public ApartmentWithMessageDto getApartmentById(@RequestHeader String token,
//                                                    @RequestParam Long id,
//                                                    @RequestParam(required = false) LocalDate start,
//                                                    @RequestParam(required = false) LocalDate end,
//                                                    @RequestParam(required = false) String promoCode) {
//        if (isNull(authService.checkValidToken(token))) {
//            return new ApartmentWithMessageDto(SIGN_IN, null);
//        }
//
//        if (isNull(start) && isNull(end)) {
//            return rentApartmentService.getApartmentById(id);
//        }
//        if (nonNull(start) && nonNull(end)) {
//            return rentApartmentService.bookApartment(id, start, end, promoCode, token);
//        } else return new ApartmentWithMessageDto();
//    }


    /**
     * Тест метода контроллера getAddressInfo который ищет по имени города.
     * */
    @Test
    public void getAddressInfoTest() throws Exception {
        mockMvc.perform(get(GET_APARTMENT_INFO)
                        .param("cityName" , "Омск"))
                .andExpect(jsonPath("$.apartmentDtoList").isNotEmpty())
                .andExpect(status().is(200));
    }


    /**
     * Негативный тест метода контроллера getAddressInfo который ищет по неправильному имени города.
     * */
    @Test
    public void getAddressInfoTestWithCityUnknown() throws Exception {
        mockMvc.perform(get(GET_APARTMENT_INFO)
                        .param("cityName" , "Омскч"))
                .andExpect(jsonPath("$.apartmentDtoList").isEmpty())
                .andExpect(status().is(200));
    }



    /**
     * Тест метода контроллера addNewApartment
     * */
    @Test
    public void addNewApartmentTest() throws Exception{
        mockMvc.perform(post(ADD_NEW_APARTMENT)
                .header("token", "testToken|2030-10-07T12:49:43.641604600")
                .content(asJSONstring(prepareApartmentDtoForTest()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.apartmentDto").isNotEmpty())
                .andExpect(jsonPath("$.message")
                        .value(APARTMENT_SAVED))
                .andExpect(status().is(200));

    }


    /**
     * Метод для перевода объекта в строку
     * */
    public static String asJSONstring(final Object o) {
        try {
            return new ObjectMapper().writeValueAsString(o);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }


//        @PostMapping(ADD_NEW_APARTMENT)
//        public ApartmentWithMessageDto addNewApartment(@RequestHeader String token,
//                @RequestBody ApartmentDto apartmentDto) {
//
//            if (!authService.checkValidToken(token)) {
//                return new ApartmentWithMessageDto(SIGN_IN, null);
//            }
//
//            return rentApartmentService.registrationNewApartment(apartmentDto, token);
//        }



//    @PostMapping(GET_APARTMENT_BY_LOCATION)
//    public GetAddressInfoResponseDto getApartmentsByLocation(@Valid @RequestBody PersonsLocation location) {
//        return rentApartmentService.getApartmentsByLocation(location);
//    }

    }
}
