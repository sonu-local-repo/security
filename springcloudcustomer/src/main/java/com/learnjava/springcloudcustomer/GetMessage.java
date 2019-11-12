package com.learnjava.springcloudcustomer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/customer")
public class GetMessage {

    @Autowired
    RestTemplate restTemplate;

    @GetMapping("/message")
    public String getMessage(){
        return "Message from Customer Service Get Message";
    }
    @GetMapping("/user/message")
    public String getUsers(){
        return restTemplate.getForObject("http://USER-SERVICE/user/message", String.class);
    }
}
