package com.example.rent_module.integration;

import com.example.rent_module.application_exceptions.IntegrationConfigurationException;
import com.example.rent_module.model.dto.PersonsLocation;
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

@Service
@RequiredArgsConstructor
public class YandexWeatherRestTemplateManager {

    private final IntegrationRepository integrationRepository;

    public String getWeatherByLocation(String latitude, String longitude) {

        RestTemplate restTemplate = new RestTemplate();

        IntegrationInfoEntity config = integrationRepository.findById(2l)
                .orElseThrow(() -> new IntegrationConfigurationException());

        YandexWeatherResponse weather = restTemplate.exchange(String.format(config.getServicePath(),
                        latitude,
                        longitude),
                HttpMethod.GET,
                new HttpEntity<>(prepareHeadersForWeather(config)),
                YandexWeatherResponse.class).getBody();

        return parseWeatherByLocation(weather);

    }

    //TODO не понимаю этот метод.
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
}
