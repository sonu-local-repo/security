package com.learnjava.springgatewayservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableEurekaClient
public class SpringGatewayServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringGatewayServiceApplication.class, args);
    }

}
