package com.example.rent_module.integration;


import com.example.rent_module.application_exceptions.IntegrationConfigurationException;
import com.example.rent_module.model.dto.PersonsLocation;
import com.example.rent_module.model.dto.geocoder.Components;
import com.example.rent_module.model.dto.geocoder.GeoCoderResponse;
import com.example.rent_module.model.dto.geocoder.ResultIndexElement;
import com.example.rent_module.model.dto.yandex_weather_ntegration.FactWeather;
import com.example.rent_module.model.dto.yandex_weather_ntegration.YandexWeatherResponse;
import com.example.rent_module.model.entity.IntegrationInfoEntity;
import com.example.rent_module.repository.IntegrationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static com.example.rent_module.constant_project.ConstantProject.CITY_NO_EXISTS;

@Service
@RequiredArgsConstructor
public class GeoCoderRestTemplateManager {

    private final IntegrationRepository integrationRepository;

    public String getInfoByLocation(PersonsLocation location) {

        RestTemplate restTemplate = new RestTemplate();
        IntegrationInfoEntity config = integrationRepository.findById(1l)
                .orElseThrow(() -> new IntegrationConfigurationException());

        GeoCoderResponse locationInfo = restTemplate.exchange(String.format(config.getServicePath(),
                        location.getLatitude(),
                        location.getLongitude(),
                        config.getServiceToken()),
                HttpMethod.GET,
                new HttpEntity<>(null),
                GeoCoderResponse.class).getBody();
        return parseInfoByLocation(locationInfo);
    }


    public YandexWeatherResponse getWeatherByLocation(PersonsLocation location) {

        RestTemplate restTemplate = new RestTemplate();
        IntegrationInfoEntity config = integrationRepository.findById(2l)
                .orElseThrow(() -> new IntegrationConfigurationException());

        YandexWeatherResponse weather = restTemplate.exchange(String.format(config.getServicePath(),
                        location.getLatitude(),
                        location.getLongitude()),
                HttpMethod.GET,
                new HttpEntity<>(prepareHeadersForWeather(config)),
                YandexWeatherResponse.class).getBody();

        return weather;
    }

    private MultiValueMap prepareHeadersForWeather(IntegrationInfoEntity config) {

        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap();

        multiValueMap.add(config.getServiceId(), config.getServiceToken());

        return multiValueMap;



    }

    private String parseWeatherByLocation(YandexWeatherResponse yandexWeatherResponse) {
        FactWeather factWeather = yandexWeatherResponse.getFactWeather();
        String temp = factWeather.getTemp();
        String condition = factWeather.getCondition();
        return condition + ", температура воздуха " + temp + " градуса";
    }

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
