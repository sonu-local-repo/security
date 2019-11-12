
package com.javalearn.gatewayservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.gateway.discovery.DiscoveryClientRouteDefinitionLocator;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.gateway.route.builder.UriSpec;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.function.Function;

@Configuration
public class GatewayConfig {

    @Autowired
    Environment environment;

    @Bean
    public RouteLocator routeLocator (RouteLocatorBuilder routeLocatorBuilder) {
        System.out.println("service called");
        return routeLocatorBuilder.routes()
                .route(r->r.path("/user/**")
                        .filters(f->f.filter(new JWTFilter(environment)))
//                        .uri("lb://USER-SERVICE/")
                        .uri("http://localhost:8094/")

                        .id("employeeModule"))
                .route(r->r.path("/customer/**")
                .uri("lb://CUSTOMER-SERVICE/")
                        .id("customer-service"))
                .route(r->r.path("/**")
//                        .uri("lb://USER-SERVICE/")
                .uri("http://localhost:8094/")
                .id("employeeModuleLogin"))
                .build();
    }
}
