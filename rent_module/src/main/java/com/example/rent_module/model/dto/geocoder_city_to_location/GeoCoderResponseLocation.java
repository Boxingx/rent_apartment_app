package com.example.rent_module.model.dto.geocoder_city_to_location;

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
public class GeoCoderResponseLocation {

    @JsonProperty(value = "results")
    private List<ResultsLoc> resultsLoc;
}
