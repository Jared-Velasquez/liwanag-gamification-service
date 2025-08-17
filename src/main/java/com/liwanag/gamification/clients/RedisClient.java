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
    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisTemplate<String, String> strRedisTemplate;
    private final CircuitBreakerRegistry registry;

    public RedisClient(
            @Qualifier("redisTemplate") RedisTemplate<String, Object> redisTemplate,
            @Qualifier("strRedisTemplate") RedisTemplate<String, String> strRedisTemplate,
            CircuitBreakerRegistry registry
    ) {
        this.redisTemplate = redisTemplate;
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
        withCircuitBreaker(
                () -> {
                    strRedisTemplate.opsForValue().set(key, value, Duration.ofSeconds(ttl));
                    return null;
                },
                () -> null
        );
    }

    public <T> Optional<T> hGet(String key, Class<T> type) {
        return withCircuitBreaker(() -> {
                Object value = redisTemplate.opsForValue().get(key);
                if (!type.isInstance(value)) {
                    log.warn("Unexpected type from Redis. Expected {}, got {}", type, value != null ? value.getClass() : "null");
                    return Optional.empty();
                }

                return Optional.of(type.cast(value));
            },
            Optional::empty
        );
    }

    public <T> void hSet(String key, T value, Long ttl) {
        withCircuitBreaker(
                () -> {
                    redisTemplate.opsForValue().set(key, value, Duration.ofSeconds(ttl));
                    return null;
                },
                () -> null
        );
    }

    public void invalidateKey(String key) {
        withCircuitBreaker(
                () -> {
                    redisTemplate.delete(key);
                    return null;
                },
                () -> null
        );
    }
}
