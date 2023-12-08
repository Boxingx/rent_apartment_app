package com.example.proxy_module;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ProxyModuleApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProxyModuleApplication.class, args);
	}

}
