package com.liwanag.gamification.service.xp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
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

        if (xpRedisService.isRedisUp()) {
            log.info("Checking Redis for user {}'s XP", userId.toString());
            xp = xpRedisService.getUserXp(userId);
        } else
            log.warn("Redis is down, falling back to database for user {}", userId);

        // If found in Redis, return it
        if (xp != null) {
            log.info("Found XP in Redis for user {}: {}", userId, xp);
            return xp;
        }

        // If not found in Redis, fetch from the canonical database
        if (!xpDatabaseService.isDatabaseUp()) {
            log.error("Database is down, cannot retrieve XP for user {}", userId);
            return null;
        }

        // Fetch from the database and update Redis
        log.info("Fetching XP from database for user {}" , userId.toString());
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
        if (xpRedisService.isRedisUp()) {
            xpRedisService.incrementUserXp(userId, deltaXp);
        } else if (xpDatabaseService.isDatabaseUp()) {
            // If Redis is down, increment in the database
            log.warn("Redis is down, incrementing XP in database for user {}", userId);
            xpDatabaseService.incrementUserXp(userId, deltaXp, Instant.now());
        } else {
            log.error("Both Redis and Database are down, cannot increment XP for user {}", userId);
        }
    }
}
