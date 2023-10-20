package com.example.rent_module.integration;


import com.example.rent_module.application_exceptions.IntegrationConfigurationException;
import com.example.rent_module.application_exceptions.LocationException;
import com.example.rent_module.model.dto.PersonsLocation;
import com.example.rent_module.model.dto.geocoder.Components;
import com.example.rent_module.model.dto.geocoder.GeoCoderResponse;
import com.example.rent_module.model.dto.geocoder.ResultIndexElement;
import com.example.rent_module.model.dto.geocoder_city_to_location.GeoCoderResponseLocation;
import com.example.rent_module.model.dto.geocoder_city_to_location.Geometry;
import com.example.rent_module.model.dto.geocoder_city_to_location.ResultsLoc;
import com.example.rent_module.model.entity.IntegrationInfoEntity;
import com.example.rent_module.repository.IntegrationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static com.example.rent_module.base64.ApplicationEncoderDecoder.decode;
import static com.example.rent_module.constant_project.ConstantProject.*;
import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class GeoCoderRestTemplateManagerImpl implements GeoCoderRestTemplateManager {

    private final IntegrationRepository integrationRepository;


    /**Метод для получения города по локации с использованием интеграционного сервиса геокодер*/
    public String getInfoByLocation(PersonsLocation location) {
        RestTemplate restTemplate = new RestTemplate();

        IntegrationInfoEntity config = integrationRepository.findById(1l)
                .orElseThrow(() -> new IntegrationConfigurationException(ERROR_DESCRIPTION));

        GeoCoderResponse locationInfo = restTemplate.exchange(String.format(config.getServicePath(),
                        location.getLatitude(),
                        location.getLongitude(),
                        decode(config.getServiceToken())),
                HttpMethod.GET,
                new HttpEntity<>(null),
                GeoCoderResponse.class).getBody();

        return parseInfoByLocation(locationInfo);
    }


    /**Метод для получения широты и долготы по городу с использованием интеграционного сервиса геокодер*/
    public Geometry getLocationByCity(String city, String country) {
        RestTemplate restTemplate = new RestTemplate();

        IntegrationInfoEntity config = integrationRepository.findById(3l)
                .orElseThrow(() -> new IntegrationConfigurationException(ERROR_DESCRIPTION));

        GeoCoderResponseLocation geoCoderResponse = restTemplate.exchange(String.format(config.getServicePath(),
                        city,
                        country,
                        decode(config.getServiceToken())),
                HttpMethod.GET,
                new HttpEntity<>(null),
                GeoCoderResponseLocation.class).getBody();

        return getGeometryByLocation(geoCoderResponse);
    }


    /**Метод для того что бы достать из ответа геокодера который в принимаемых значениях только объект с широтой и долготой который мы
     * возвращаем*/
    private Geometry getGeometryByLocation(GeoCoderResponseLocation geoCoderResponse) {
        ResultsLoc resultsLoc = geoCoderResponse.getResultsLoc().get(0);
        Geometry geometry = resultsLoc.getGeometry();
        if(isNull(geometry)) {
            throw new LocationException(LOCATION_ERROR);
        }

        return geometry;
    }



    /**Метод для того что бы достать из ответа геокодера который в принимаемых значениях только город в который мы возвращаем в строке*/
    private String parseInfoByLocation(GeoCoderResponse locationInfo) {
        List<ResultIndexElement> resultsObject = locationInfo.getResultsObject();
        ResultIndexElement resultIndexElement = resultsObject.get(0);
        Components components = resultIndexElement.getComponents();
        if (components.getCity() != null) {
            return components.getCity();
        } else if (components.getTown() != null) {
            return components.getTown();
        }
        return CITY_NO_EXISTS;
    }
}
