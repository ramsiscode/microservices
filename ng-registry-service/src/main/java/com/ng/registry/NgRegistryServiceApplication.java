package com.ng.registry.ngregistryservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class NgRegistryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(NgRegistryServiceApplication.class, args);
	}

}
