package com.example.rent_module.model.dto.geocoder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true, value = {"error"})
@Getter
@Setter
@ToString
/**
 * Класс создан что бы распарсить Json, для получения только города.
 * GeoCoderResponse -> ResultIndexElement -> Components
 * */
public class GeoCoderResponse {

    @JsonProperty(value = "results")
    private List<ResultIndexElement> resultsObject;
}
