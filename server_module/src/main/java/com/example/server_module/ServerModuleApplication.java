package com.example.server_module;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class ServerModuleApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerModuleApplication.class, args);
    }

}
