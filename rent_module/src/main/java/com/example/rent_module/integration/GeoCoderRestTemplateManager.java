package com.example.rent_module.integration;

import com.example.rent_module.model.dto.PersonsLocation;
import com.example.rent_module.model.dto.geocoder_city_to_location.Geometry;

public interface GeoCoderRestTemplateManager {

    String getInfoByLocation(PersonsLocation location);

    Geometry getLocationByCity(String city, String country);

}
