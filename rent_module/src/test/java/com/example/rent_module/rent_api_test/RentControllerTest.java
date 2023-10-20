package com.example.rent_module.rent_api_test;

import com.example.rent_module.integration.GeoCoderRestTemplateManager;
import com.example.rent_module.integration.YandexWeatherRestTemplateManagerImpl;
import com.example.rent_module.model.dto.ApartmentWithMessageDto;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

import static com.example.rent_module.constant_project.ConstantProject.*;
import static com.example.rent_module.rent_api_test.PrepareObjectToTest.*;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class RentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GeoCoderRestTemplateManager restTemplateManager;

//    @MockBean
//    private YandexWeatherRestTemplateManagerImpl yandexWeatherRestTemplateManager;

    /**
     * Тест метода выгрузки апартаментов по локации
     */
    @Test
    public void getApartmentByLocationTest() throws Exception {

        Mockito.when(restTemplateManager.getInfoByLocation(preparePersonLocationForTest())).thenReturn("Омск");

//        Mockito.when(yandexWeatherRestTemplateManager.getWeatherByLocation(preparePersonLocationForTest())).thenReturn(prepareYandexWeatherResponseForTest());

        mockMvc.perform(post(GET_APARTMENT_BY_LOCATION)
                        .content(asJSONstring(preparePersonLocationForTest()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.exceptionMessage")
                        .value("Нет квартир в этом городе"))
                .andExpect(jsonPath("$.condition")
                        .value("облачно"))
                .andExpect(status().is(200));
    }

    @Test
    public void findApartmentByParam() throws Exception {
        mockMvc.perform(get(GET_APARTMENT_BY_ID)
                .param("id", "5"))
                .andExpect(jsonPath("$.value")
                        .value(""))
                .andExpect(status().is(200));
    }

//    @GetMapping(GET_APARTMENT_BY_ID)
//    public ApartmentWithMessageDto getApartmentById(@RequestParam Long id,
//                                                    @RequestParam(required = false) LocalDate start,
//                                                    @RequestParam(required = false) LocalDate end,
//                                                    @RequestParam(required = false) String promoCode) {
//        if (isNull(userSession.getNickName())) {
//            return new ApartmentWithMessageDto(SIGN_IN, null);
//        }
//
//        if (isNull(start) && isNull(end)) {
//            return rentApartmentService.getApartmentById(id);
//        }
//        if (nonNull(start) && nonNull(end)) {
//            return rentApartmentService.bookApartment(id, start, end, promoCode);
//        } else return new ApartmentWithMessageDto();
//    }

    public static String asJSONstring(final Object o) {
        try {
            return new ObjectMapper().writeValueAsString(o);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

//    @PostMapping(GET_APARTMENT_BY_LOCATION)
//    public GetAddressInfoResponseDto getApartmentsByLocation(@Valid @RequestBody PersonsLocation location) {
//        return rentApartmentService.getApartmentsByLocation(location);
//    }

    }
}
