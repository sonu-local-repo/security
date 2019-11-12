package com.javalearn.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;


    @PostMapping("/create")
    public User createCustomer(@RequestBody final UserModel customerModel) {

        return userService.createUser(customerModel);
    }
    @GetMapping("/verifyuser")
    public String verifyUser() {
        return "this is working fine finally";
    }

}
