package com.learnjava.springgatewayservice.resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class GetMessage {

    @GetMapping("/message")
    public String getMessage(){
        return "Message from User Service Get Message";
    }
}

