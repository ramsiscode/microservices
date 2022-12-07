package com.ng.gateway.config;

import lombok.extern.slf4j.Slf4j;
import com.ng.gateway.filter.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;


@Configuration
@Slf4j
public class GatewayConfig  {

    @Autowired
    private JwtAuthenticationFilter filter;

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
    	return builder.routes()
                .route("ng-auth-services", r -> r
                    .path("/auth/**")
                    .filters(f -> f
                        .filter(filter)
                        //.circuitBreaker(config -> config
                        //    .setName("ng-auth-services")
                        //    .setFallbackUri("forward:/ngAuthService"))
                        )
                    .uri("lb://ng-auth-services"))
                .build();
    }
}



