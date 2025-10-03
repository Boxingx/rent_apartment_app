package com.example.rent_module.integration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

@Service
public class ProductRestTemplateManager {

    private final static Logger logger = LoggerFactory.getLogger(ProductRestTemplateManager.class);


    /**
     * Метод прокидывает запрос в продуктовый модуль(product_module), принимает id бронирования и погоду, а возвращает финальный платеж в
     * формате Double. В ProductModelController вызывается метод addProductForBooking.
     */
    public Double prepareProduct(@RequestParam Long id, @RequestParam String weather) {

        logger.info("Класс ProductRestTemplateManager метод prepareProduct начал выполнять REST-запрос id = {} , weather = {}", id, weather);

        RestTemplate restTemplate = new RestTemplate();

        String path = "http://localhost:8098/product/add?id=%s&weather=%s";
        Double body = restTemplate.exchange(String.format(path, id, weather),
                HttpMethod.GET,
                new HttpEntity<>(null),
                Double.class).getBody();

        logger.info("Класс ProductRestTemplateManager метод prepareProduct успешно выполнил REST-запрос id = {} , weather = {}", id, weather);
        return body;
    }
}
