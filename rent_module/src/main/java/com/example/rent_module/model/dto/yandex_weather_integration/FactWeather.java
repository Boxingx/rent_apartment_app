package com.example.rent_module.model.dto.yandex_weather_integration;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;


@JsonIgnoreProperties(ignoreUnknown = true, value = {"error"})
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class FactWeather {

    @JsonProperty(value = "temp")
    private String temp;

    @JsonProperty(value = "condition")
    private String condition;
}
