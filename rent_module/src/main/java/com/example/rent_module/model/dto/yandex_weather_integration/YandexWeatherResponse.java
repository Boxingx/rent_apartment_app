package com.example.rent_module.model.dto.yandex_weather_integration;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@JsonIgnoreProperties(ignoreUnknown = true, value = {"error"})
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
/**
 * Класс создан что бы распарсить Json, для получения погоды.
 * YandexWeatherResponse -> FactWeather
 * */
public class YandexWeatherResponse {

    @JsonProperty(value = "fact")
    private FactWeather factWeather;

}
