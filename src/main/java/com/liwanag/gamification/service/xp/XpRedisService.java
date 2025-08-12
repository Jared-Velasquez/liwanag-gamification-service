package com.liwanag.gamification.service.xp;

import com.liwanag.gamification.model.UserXp;
import com.liwanag.gamification.utils.Redis;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class XpRedisService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final Duration XP_TTL = Duration.ofMinutes(15);

    @Transactional
    public void incrementUserXp(UUID userId, Integer deltaXp) {
        log.info("Incrementing XP in Redis for user {} by {}", userId, deltaXp);
        String key = Redis.generateUserXpKey(userId);
        redisTemplate.opsForValue().increment(key, deltaXp);
        redisTemplate.expire(key, XP_TTL);

        // Mark user as dirty
        redisTemplate.opsForSet().add("dirty_xp_users", userId);
    }

    public Integer getUserXp(UUID userId) {
        log.info("Fetching XP from Redis for user {}", userId.toString());
        String key = Redis.generateUserXpKey(userId);
        return redisTemplate.opsForValue().get(key) != null ?
               (Integer) redisTemplate.opsForValue().get(key) : null;
    }

    @Transactional
    public void setUserXp(UUID userId, Integer xp) {
        log.info("Setting XP in Redis for user {} to {}", userId, xp);
        String key = Redis.generateUserXpKey(userId);
        redisTemplate.opsForValue().set(key, xp, XP_TTL);

        // Mark user as dirty
        redisTemplate.opsForSet().add("dirty_xp_users", userId);
    }

    public List<UserXp> getAllUserXps() {
        return redisTemplate.keys("user:*:xp").stream()
                .map(key -> {
                    UUID userId = UUID.fromString(key.replace("user:", "").replace(":xp", ""));
                    Integer xp = getUserXp(userId);
                    return new UserXp(userId, xp, Instant.now());
                })
                .toList();
    }

    public List<UserXp> getAllDirtyUserXps() {
        log.info("Fetching all dirty user XPs from Redis");
        return redisTemplate.opsForSet().members("dirty_xp_users").stream()
                .map(userIdObj -> {
                    UUID userId = UUID.fromString(userIdObj.toString());
                    Integer xp = getUserXp(userId);
                    return new UserXp(userId, xp, Instant.now());
                })
                .toList();
    }

    @Transactional
    public void clearDirtyUsers() {
        redisTemplate.delete("dirty_xp_users");
    }

    public boolean isRedisUp() {
        try {
            return redisTemplate.getConnectionFactory().getConnection().ping() != null;
        } catch (Exception e) {
            return false;
        }
    }
}
