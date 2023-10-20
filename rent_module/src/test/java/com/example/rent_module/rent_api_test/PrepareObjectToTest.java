package com.example.rent_module.rent_api_test;

import com.example.rent_module.model.dto.PersonsLocation;
import com.example.rent_module.model.dto.yandex_weather_integration.FactWeather;
import com.example.rent_module.model.dto.yandex_weather_integration.YandexWeatherResponse;

public class PrepareObjectToTest {

    public final static String LATITUDE = "54.973609";

    public final static String LONGITUDE = "73.371201";

    public static PersonsLocation preparePersonLocationForTest() {
        return new PersonsLocation(LATITUDE, LONGITUDE);
    }

    public static YandexWeatherResponse prepareYandexWeatherResponseForTest() {
        return new YandexWeatherResponse(new FactWeather("10", "clear"));
    }

}
