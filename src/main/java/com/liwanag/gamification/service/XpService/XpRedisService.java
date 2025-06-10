package com.liwanag.gamification.service.XpService;

import com.liwanag.gamification.model.UserXp;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class XpRedisService {
    private final RedisTemplate<String, Object> redisTemplate;

    public void incrementXp(String userId, Integer deltaXp) {
        String key = "user:" + userId + ":xp";
        redisTemplate.opsForValue().increment(key, deltaXp);
    }

    public Integer getUserXp(UUID userId) {
        String key = "user:" + userId + ":xp";
        return redisTemplate.opsForValue().get(key) != null ?
               (Integer) redisTemplate.opsForValue().get(key) : null;
    }

    public void setUserXp(UUID userId, Integer xp) {
        String key = "user:" + userId + ":xp";
        redisTemplate.opsForValue().set(key, xp);
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

    public boolean isRedisUp() {
        try {
            return redisTemplate.getConnectionFactory().getConnection().ping() != null;
        } catch (Exception e) {
            return false;
        }
    }
}
