package com.javalearn.security;

import io.jsonwebtoken.Jwts;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.server.ServerWebExchange;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    Environment environment;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, Environment environment) {
        super(authenticationManager);
        this.environment = environment;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String authorizationToken = request.getHeader("Authorization");
        if(authorizationToken ==null){
            chain.doFilter(request, response);
            return;
        }
        Authentication authenticationToken = getAuthenticationToken(authorizationToken);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        chain.doFilter(request, response);
    }
/*    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus)  {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);

        return response.setComplete();
    }*/

    private UsernamePasswordAuthenticationToken getAuthenticationToken(String authorizationToken) {
        String token = authorizationToken.replace("Bearer ", "");
        String username = Jwts.parser()
                .setSigningKey("sadfhasdfalsdkfa;hsgdajldaslfkda")
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        if (username == null) {
            return null;
        }
        return new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>());
    }
}