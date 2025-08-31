package com.liwanag.gamification.clients;

import com.liwanag.gamification.utils.KeyObjectPair;
import com.liwanag.gamification.utils.TimeConstants;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.stereotype.Component;
import io.vavr.control.Try;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;

import java.time.Duration;
import java.util.*;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;

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

    public <T> Optional<T> objGet(String key, Class<T> type) {
        return withCircuitBreaker(() -> {
                Object value = redisTemplate.opsForValue().get(key);
                if (value == null) return Optional.empty();

                if (!type.isInstance(value)) {
                    log.warn("Unexpected type from Redis. Expected {}, got {}", type, value.getClass());
                    throw new SerializationException("Error deserializing Redis data");
                }

                return Optional.of(type.cast(value));
            },
            Optional::empty
        );
    }

    public <T> List<T> objGetBulk(List<String> keys, Class<T> type) {
        return withCircuitBreaker(() -> {
                List<Object> raw = redisTemplate.executePipelined(new SessionCallback<Object>() {
                    @Override
                    public Object execute(RedisOperations operations) {
                        ValueOperations<String, T> vops = operations.opsForValue();

                        for (String key : keys) {
                            vops.get(key);
                        }

                        return null;
                    }
                });

                List<T> results = new ArrayList<>();

                for (Object obj : raw) {
                    if (obj == null) { results.add(null); continue; }
                    if (!type.isInstance(obj)) {
                        throw new SerializationException("Expected " + type.getName() + " but got " + obj.getClass().getName());
                    }
                    results.add(type.cast(obj));
                }
                return results;
            },
            List::of
        );
    }

    public <T> void objSet(String key, T value, Long ttl) {
        withCircuitBreaker(() -> {
                    redisTemplate.opsForValue().set(key, value, Duration.ofSeconds(ttl));
                    return null;
                },
                () -> null
        );
    }

    public <T> void objSetBulk(List<KeyObjectPair<T>> pairs, Long ttl) {
        withCircuitBreaker(
                () -> redisTemplate.executePipelined(new SessionCallback<Void>() {
                    @Override
                    public Void execute(RedisOperations operations) {
                        ValueOperations<String, T> vops = (ValueOperations<String, T>) operations.opsForValue();
                        for (KeyObjectPair<T> pair : pairs) {
                            vops.set(pair.getKey(), pair.getObj(), Duration.ofSeconds(ttl));
                        }
                        return null;
                    }
                }),
                () -> null
        );
    }

    public <T> long zaddList(String key, List<T> items, ToDoubleFunction<T> scoreFn, Duration ttl) {
        ZSetOperations<String, Object> zops = redisTemplate.opsForZSet();

        Set<ZSetOperations.TypedTuple<Object>> tuples = new LinkedHashSet<>(items.size());
        for (T item : items) {
            tuples.add(new DefaultTypedTuple<>(item, scoreFn.applyAsDouble(item)));
        }

        Long added = zops.add(key, tuples);
        if (ttl != null && !ttl.isZero() && !ttl.isNegative())
            redisTemplate.expire(key, ttl);

        return added == null ? 0L : added;
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
