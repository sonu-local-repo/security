package com.learnjava.springgatewayservice.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/user")
public class GetMessage {

    @Autowired
    RestTemplate restTemplate;
    @GetMapping("/message")
    public String getMessage(){
        return "Message from User Service Get Message";
    }

    @GetMapping("/customer/message")
    public String getCustomer() {
        String outPut = restTemplate.getForObject("http://customer-service/customer/message", String.class);
        return outPut.concat("\n*****from User Service******************");
    }
}

