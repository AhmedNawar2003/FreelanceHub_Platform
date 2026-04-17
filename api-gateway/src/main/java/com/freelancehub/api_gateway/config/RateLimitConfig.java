package com.freelancehub.api_gateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import reactor.core.publisher.Mono;

@Configuration
public class RateLimitConfig {

    @Bean
    @Primary
    public KeyResolver ipKeyResolver() {
        return exchange -> Mono.just(
                exchange.getRequest().getRemoteAddress() != null
                        ? exchange.getRequest().getRemoteAddress()
                        .getAddress().getHostAddress()
                        : "unknown"
        );
    }

    @Bean
    public KeyResolver userKeyResolver() {
        return exchange -> {
            String auth = exchange.getRequest()
                    .getHeaders().getFirst("Authorization");
            if (auth != null && auth.startsWith("Bearer ")) {
                return Mono.just(auth.substring(7, Math.min(auth.length(), 30)));
            }
            return Mono.just("anonymous");
        };
    }

    @Bean
    @Primary
    public RedisRateLimiter publicRateLimiter() {
        return new RedisRateLimiter(20, 40, 1);
    }

    @Bean
    public RedisRateLimiter privateRateLimiter() {
        return new RedisRateLimiter(50, 100, 1);
    }
}
