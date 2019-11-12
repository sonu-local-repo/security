
package com.javalearn.gatewayservice.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.List;
import java.util.function.Function;


@Component
public class JWTFilter extends AbstractGatewayFilterFactory<JWTFilter.Config> implements GatewayFilter {

    Environment environment;
    public JWTFilter(Environment environment) {
        super(Config.class);
        this.environment = environment;
    }

    private static final Logger logger = LoggerFactory.getLogger(JWTFilter.class);


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        System.out.println("gateway filter");
        ServerHttpRequest request = exchange.getRequest().mutate().header("Added-header", "12")
                .build();
        HttpHeaders values = exchange.getRequest().getHeaders();
        if (!(exchange.getRequest().getPath().equals("login"))) {
            List<String> jwtToken = values.getValuesAsList("Authorization");
            System.out.println("gateway filter path if " + jwtToken);;
            if (jwtToken.size() == 0) {
                return this.onError(exchange, "No Authorization header", HttpStatus.UNAUTHORIZED);
            }else {
                System.out.println(getUsernameFromToken(jwtToken.get(0)));

                return chain.filter(exchange.mutate().request(request).build());
            }
        }
        return chain.filter(exchange.mutate().request(request).build());
    }

    public static class Config {
        // Put the configuration properties
    }


    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            HttpHeaders values = exchange.getRequest().getHeaders();
            List<String> jwtToken = values.getValuesAsList("Authorization");
            if (jwtToken.size() == 0) {
                return this.onError(exchange, "No Authorization header", HttpStatus.UNAUTHORIZED);
            }else {
                System.out.println(getUsernameFromToken(jwtToken.get(0)));
                ServerHttpRequest request = exchange.getRequest().mutate().header("Added-header", "12")
                        .build();
                return chain.filter(exchange.mutate().request(request).build());
            }

        };
    }

    private String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }
    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        Date expiration = claims.getExpiration();
        System.out.println("Expiration "+expiration);
        if (expiration.before(new Date())) {
            System.out.println("token expired");
            throw new ExpiredJwtException(null,claims,"Token Expired");
        }
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        String updatedToken = token.replace("Bearer ", "");
        try{
            return Jwts.parser().setSigningKey(environment.getProperty("secretKey")).parseClaimsJws(updatedToken).getBody();
        }catch (Exception e){
            throw new MalformedJwtException("Invalid Token");
        }

    }
    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus)  {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }
}


