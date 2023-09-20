package com.example.rent_module.model.dto.geocoder_city_to_location;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@JsonIgnoreProperties(ignoreUnknown = true, value = {"error"})
@Getter
@Setter
@ToString
public class ResultsLoc {

    @JsonProperty(value = "geometry")
    private Geometry geometry;
}
