package com.liwanag.gamification.clients;

import com.liwanag.gamification.utils.TimeConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import io.vavr.control.Try;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;

import java.util.Optional;
import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
@Slf4j
public class RedisClient {
    private final RedisTemplate<String, Object> redisTemplate;
    private final CircuitBreakerRegistry registry;

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
                () -> Optional.ofNullable((String) redisTemplate.opsForValue().get(key)),
                Optional::empty
        );
    }

    public void vSet(String key, String value, Long ttl) {
        withCircuitBreaker(
                () -> {
                    redisTemplate.opsForValue().set(key, value, ttl);
                    return null;
                },
                () -> null
        );
    }
}
