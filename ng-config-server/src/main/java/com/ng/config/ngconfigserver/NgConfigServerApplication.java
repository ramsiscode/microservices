package com.ng.config.ngconfigserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@EnableConfigServer
@SpringBootApplication
public class NgConfigServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(NgConfigServerApplication.class, args);
	}

}
