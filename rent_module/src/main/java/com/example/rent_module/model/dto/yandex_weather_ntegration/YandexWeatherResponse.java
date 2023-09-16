package com.example.rent_module.model.dto.yandex_weather_ntegration;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@JsonIgnoreProperties(ignoreUnknown = true, value = {"error"})
@Getter
@Setter
@ToString
public class YandexWeatherResponse {

    @JsonProperty(value = "fact")
    private FactWeather factWeather;

}
