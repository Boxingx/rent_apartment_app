package com.example.rent_module.integration;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

@Service
public class ProductRestTemplateManager {

    public Double prepareProduct(@RequestParam Long id, @RequestParam String weather) {

        RestTemplate restTemplate = new RestTemplate();

        String path = "http://localhost:8097/product/add?id=%s&weather=%s";
            Double body = restTemplate.exchange(String.format(path, id, weather),
                    HttpMethod.GET,
                    new HttpEntity<>(null),
                    Double.class).getBody();
            return body;
    }
}
