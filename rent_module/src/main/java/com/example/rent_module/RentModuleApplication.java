package com.example.rent_module;

import com.example.rent_module.application_exceptions.IntegrationConfigurationException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class RentModuleApplication {

    public static void main(String[] args) {
        SpringApplication.run(RentModuleApplication.class, args);
    }

}
