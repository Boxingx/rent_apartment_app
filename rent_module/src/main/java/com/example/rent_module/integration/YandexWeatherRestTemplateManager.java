package com.example.rent_module.integration;

import com.example.rent_module.model.dto.PersonsLocation;
import com.example.rent_module.model.dto.yandex_weather_integration.YandexWeatherResponse;

public interface YandexWeatherRestTemplateManager {

    String getWeatherByCoordinates(String latitude, String longitude);

    YandexWeatherResponse getWeatherByLocation(PersonsLocation location);

}
