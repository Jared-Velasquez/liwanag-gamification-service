package com.liwanag.gamification.task;

import com.liwanag.gamification.model.UserXp;
import com.liwanag.gamification.service.XpService.XpDatabaseService;
import com.liwanag.gamification.service.XpService.XpRedisService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@RequiredArgsConstructor
public class ScheduledTasks {
    private final XpRedisService xpRedisService;
    private final XpDatabaseService xpDatabaseService;
    // Three minutes
    private static final Integer SYNC_XP_DATABASE = 3 * 60 * 1000;

    // Using Write-back/Write-behind caching strategy
    @Scheduled(fixedRate = SYNC_XP_DATABASE)
    @Transactional
    public void syncXpDatabase() {
        // Logic to sync XP from Redis to the database
        // This could involve fetching all user XP from Redis and saving them to the database
        // For example:
        List<UserXp> userXps = xpRedisService.getAllUserXps();
        // userXps.forEach(userXp -> xpDatabaseService.saveUserXp(userXp.getUserId(), userXp.getXp()));
    }
}
