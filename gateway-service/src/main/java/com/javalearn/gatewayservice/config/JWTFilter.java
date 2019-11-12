
package com.javalearn.gatewayservice.config;

import io.jsonwebtoken.*;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
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

import static java.lang.System.*;


@Component
public class JWTFilter extends AbstractGatewayFilterFactory<JWTFilter.Config> {

    public JWTFilter() {
        super(Config.class);
    }
    public static class Config {
        // Put the configuration properties
    }

    @Value("${jwt.secret}")
    private String secret;
    private static final Logger logger = LoggerFactory.getLogger(JWTFilter.class);

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            HttpHeaders values = exchange.getRequest().getHeaders();
            List<String> jwtToken = values.getValuesAsList("Authorization");
            if (jwtToken.size() == 0) {
                return this.onError(exchange, "No Authorization header", HttpStatus.UNAUTHORIZED);
            }else {
                out.println(getUsernameFromToken(jwtToken.get(0)));
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
        if (expiration.after(new Date())) {
            System.out.println("token expired");
            throw new ExpiredJwtException(null,claims,"Token Expired");
        }
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        String updatedToken = token.replace("Bearer ", "");
        try{
            return Jwts.parser().setSigningKey(secret).parseClaimsJws(updatedToken).getBody();
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


