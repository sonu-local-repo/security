
package com.javalearn.gatewayservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.gateway.discovery.DiscoveryClientRouteDefinitionLocator;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/*@Configuration
public class GatewayConfig {



    @Bean
    public RouteLocator routeLocator (RouteLocatorBuilder routeLocatorBuilder) {
        System.out.println("service called");
        return routeLocatorBuilder.routes()
                .route(r->r.path("/user/**")
//                        .uri("lb://USER-SERVICE/")
                        .uri("http://localhost:8094/").filters(new JWTFilter().apply(new JWTFilter.Config()))
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
*/
