package com.javalearn.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private Environment environment;
    private AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(Environment environment, AuthenticationManager authenticationManager) {
        this.environment = environment;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            UserModel user = new ObjectMapper()
                    .readValue(request.getInputStream(), UserModel.class);
            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword(), new ArrayList<>())
            );
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        User principal = (User)authResult.getPrincipal();
        String token = Jwts.builder()
                .setSubject(principal.getUsername())
                .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(environment.getProperty("token.expiration"))))
                .signWith(SignatureAlgorithm.HS512,environment.getProperty("token.secret"))
                .compact();
        response.addHeader("token", token);
    }
}
