package com.example.rent_module.rent_api_test;

import com.example.rent_module.integration.GeoCoderRestTemplateManager;
import com.example.rent_module.integration.YandexWeatherRestTemplateManager;
import com.example.rent_module.model.dto.PersonsLocation;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

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
    public void getApartmentByIdTest() throws Exception {
        mockMvc.perform(get(GET_APARTMENT_BY_ID)
                        .param("id", "5")
                        .header("token", "testToken|2030-10-07T12:49:43.641604600"))
                .andExpect(jsonPath("$.message")
                        .value("Квартира доступна, начать бронирование?"))
                .andExpect(jsonPath("$.apartmentDto").isNotEmpty())
                .andExpect(status().is(200));
    }


    /**
     * Тест метода контроллера getApartmentById, передается id которого нет в БД и правильный токен
     * */
    @Test
    public void getApartmentByIdNegativeTest() throws Exception {
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
    }
}
