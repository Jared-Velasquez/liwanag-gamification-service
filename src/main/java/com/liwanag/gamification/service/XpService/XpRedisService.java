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

    public Integer getXp(String userId) {
        String key = "user:" + userId + ":xp";
        return redisTemplate.opsForValue().get(key) != null ?
               (Integer) redisTemplate.opsForValue().get(key) : 0;
    }

    public List<UserXp> getAllUserXps() {
        return redisTemplate.keys("user:*:xp").stream()
                .map(key -> {
                    String userId = key.replace("user:", "").replace(":xp", "");
                    Integer xp = getXp(userId);
                    return new UserXp(UUID.fromString(userId), xp, Instant.now());
                })
                .toList();
    }
}
