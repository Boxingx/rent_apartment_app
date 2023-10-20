package com.example.rent_module.integration;

import com.example.rent_module.application_exceptions.IntegrationConfigurationException;
import com.example.rent_module.model.dto.PersonsLocation;
import com.example.rent_module.model.dto.yandex_weather_integration.FactWeather;
import com.example.rent_module.model.dto.yandex_weather_integration.YandexWeatherResponse;
import com.example.rent_module.model.entity.IntegrationInfoEntity;
import com.example.rent_module.repository.IntegrationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import static com.example.rent_module.base64.ApplicationEncoderDecoder.decode;
import static com.example.rent_module.constant_project.ConstantProject.ERROR_DESCRIPTION;

@Service
@RequiredArgsConstructor
public class YandexWeatherRestTemplateManagerImpl implements YandexWeatherRestTemplateManager {

    private final IntegrationRepository integrationRepository;

    /**Метод прокидывает запрос в сервис яндекс погоды. Принимает широту и долготу, по ней получает большой Json с лишней информацией
     * которая фильтруется с помощью приватного метода parseWeatherByLocation, после этой операции возвращается только состояние погоды
     * и температура в формате String. Хедеры загружаются с помощью метода prepareHeadersForWeather */
    public String getWeatherByCoordinates(String latitude, String longitude) {
        RestTemplate restTemplate = new RestTemplate();

        IntegrationInfoEntity config = integrationRepository.findById(2l)
                .orElseThrow(() -> new IntegrationConfigurationException(ERROR_DESCRIPTION));

        YandexWeatherResponse weather = restTemplate.exchange(String.format(config.getServicePath(),
                        latitude,
                        longitude),
                HttpMethod.GET,
                new HttpEntity<>(prepareHeadersForWeather(config)),
                YandexWeatherResponse.class).getBody();

        return parseWeatherByLocation(weather);

    }


    /**Метод прокидывает запрос в сервис яндекс погоды. Принимает объект с широтой и долготой, по ним получает объект YandexWeatherResponse
     * в котором есть состояние погоды и температура и возвращает его. Хедеры загружаются с помощью метода prepareHeadersForWeather */
    public YandexWeatherResponse getWeatherByLocation(PersonsLocation location) {
        RestTemplate restTemplate = new RestTemplate();

        IntegrationInfoEntity config = integrationRepository.findById(2l)
                .orElseThrow(() -> new IntegrationConfigurationException(ERROR_DESCRIPTION));

        YandexWeatherResponse weather = restTemplate.exchange(String.format(config.getServicePath(),
                        location.getLatitude(),
                        location.getLongitude()),
                HttpMethod.GET,
                new HttpEntity<>(prepareHeadersForWeather(config)),
                YandexWeatherResponse.class).getBody();

        return weather;
    }



    //что за MultiValueMap?
    /**Метод который подготавливает хедеры для restTemplate запроса. Создается MultiValueMap в которую помещается
     * Api Key взятый из поля serviceId у переданного конфига, а так же помещается токен взятый из поля serviceToken взятый тоже у конфига.*/
    private MultiValueMap prepareHeadersForWeather(IntegrationInfoEntity config) {

        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap();
        multiValueMap.add(config.getServiceId(), decode(config.getServiceToken()));

        return multiValueMap;
    }


    /**Метод для того что бы достать нужные данные из принимаемого YandexWeatherResponse и собрать их в строку с уведомлением о погоде,
     *  которую мы возвращаем.*/
    private String parseWeatherByLocation(YandexWeatherResponse yandexWeatherResponse) {

        FactWeather factWeather = yandexWeatherResponse.getFactWeather();
        String temp = factWeather.getTemp();
        String condition = factWeather.getCondition();

        return condition + ", температура воздуха " + temp + " градуса";

    }
}
