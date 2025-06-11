package com.liwanag.gamification.service.level;

import com.liwanag.gamification.service.xp.XpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class LevelService {
    private final XpService xpService;
    private final Double tuningConstant = 50.0;

    private Integer calculateLevel(Integer xp) {
        log.info("Calculating level for XP: {}", xp);
        if (xp < 0) {
            throw new IllegalArgumentException("XP cannot be negative");
        }

        // Simple level calculation based on XP
        return (int) Math.sqrt(xp / tuningConstant);
    }

    public boolean checkLevelUp(Integer currentXp, Integer newXp) {
        log.info("Checking level up: current XP = {}, new XP = {}", currentXp, newXp);
        if (currentXp == null || newXp == null) {
            throw new IllegalArgumentException("XP values cannot be null");
        }
        return calculateLevel(newXp) > calculateLevel(currentXp);
    }

    public Integer checkUserLevel(UUID userId) {
        log.info("Checking user level for user ID: {}", userId);
        Integer xp = xpService.getUserXp(userId);
        if (xp == null) {
            log.warn("No XP found for user ID: {}", userId);
            return null;
        }
        return calculateLevel(xp);
    }
}
