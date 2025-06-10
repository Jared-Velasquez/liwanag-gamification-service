package com.liwanag.gamification.service.XpService;

import io.lettuce.core.RedisConnectionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class XpService {
    private final XpDatabaseService xpDatabaseService;
    private final XpRedisService xpRedisService;

    public Integer getUserXp(UUID userId) {
        // Logic to get XP for a user
        // First, check if the userId exists in Redis
        Integer xp = null;

        if (xpRedisService.isRedisUp())
            xp = xpRedisService.getUserXp(userId);
        else
            log.warn("Redis is down, falling back to database for user {}", userId);

        // If found in Redis, return it
        if (xp != null)
            return xp;

        // If not found in Redis, fetch from the canonical database
        if (!xpDatabaseService.isDatabaseUp()) {
            log.error("Database is down, cannot retrieve XP for user {}", userId);
            return null;
        }

        // Fetch from the database and update Redis
        xp = xpDatabaseService.getUserXp(userId);
        xpRedisService.setUserXp(userId, xp);

        return xp;
    }

    public void incrementUserXp(UUID userId, Integer deltaXp) {
        // Logic to increment XP for a user
        if (deltaXp == null || deltaXp <= 0) {
            log.warn("Invalid XP increment value: {}", deltaXp);
            return;
        }

        // If Redis is up, only increment Redis

        // If Redis is down, fallback to database
    }
}
