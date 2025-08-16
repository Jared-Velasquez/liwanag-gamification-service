package com.liwanag.gamification.clients;

import com.liwanag.gamification.utils.TimeConstants;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import io.vavr.control.Try;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;

import java.time.Duration;
import java.util.Optional;
import java.util.function.Supplier;

@Component
@Slf4j
public class RedisClient {
    private final RedisTemplate<String, String> strRedisTemplate;
    private final CircuitBreakerRegistry registry;

    public RedisClient(
            @Qualifier("strRedisTemplate") RedisTemplate<String, String> strRedisTemplate,
            CircuitBreakerRegistry registry
    ) {
        this.strRedisTemplate = strRedisTemplate;
        this.registry = registry;
    }

    private CircuitBreaker getCircuitBreaker() {
        return registry.circuitBreaker("redisService");
    }

    private <T> T withCircuitBreaker(Supplier<T> redisCall, Supplier<T> fallback) {
        return Try.ofSupplier(
                CircuitBreaker.decorateSupplier(getCircuitBreaker(), redisCall))
                .recover(throwable -> {
                    log.warn("Redis Circuit Breaker triggered: {}", throwable.toString());
                    return fallback.get();
                })
                .get();
    }

    public Optional<String> vGet(String key) {
        return withCircuitBreaker(
                () -> Optional.ofNullable((String) strRedisTemplate.opsForValue().get(key)),
                Optional::empty
        );
    }

    public void vSet(String key, String value, Long ttl) {
        log.info("Value: {}", value);
        withCircuitBreaker(
                () -> {
                    strRedisTemplate.opsForValue().set(key, value, Duration.ofSeconds(ttl));
                    return null;
                },
                () -> null
        );
    }
}
