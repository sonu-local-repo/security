package com.javalearn.security;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    UserRepository repository;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    Environment environment;

    public User createUser(UserModel userModel) {
        System.out.println("password: "+userModel.getPassword());
        userModel.setPassword(bCryptPasswordEncoder.encode(userModel.getPassword()));
        ModelMapper mapper = new ModelMapper();
        User customer = mapper.map(userModel, User.class);
        return repository.save(customer);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findByUserName(username);
        return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword()
        ,true, true, true, true, new ArrayList<>());
    }
}
