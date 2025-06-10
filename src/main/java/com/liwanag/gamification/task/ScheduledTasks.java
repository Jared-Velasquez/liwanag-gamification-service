package com.liwanag.gamification.task;

import com.liwanag.gamification.model.UserXp;
import com.liwanag.gamification.service.xp.XpDatabaseService;
import com.liwanag.gamification.service.xp.XpRedisService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@RequiredArgsConstructor
@Slf4j
public class ScheduledTasks {
    private final XpRedisService xpRedisService;
    private final XpDatabaseService xpDatabaseService;
    // Three minutes
    private static final int SYNC_XP_DATABASE = 10 * 1000;

    @Scheduled(fixedRate = SYNC_XP_DATABASE)
    @Transactional
    public void syncXpDatabase() {
        // Using Write-back/Write-behind caching strategy
        // TODO: add error handling, perform distributed locking
        log.info("Syncing XP data from Redis to the database");
        List<UserXp> userXps = xpRedisService.getAllDirtyUserXps();
        if (userXps.isEmpty()) {
            log.info("No dirty user XPs found in Redis, skipping sync");
            return;
        }

        log.info("Found {} dirty user XPs in Redis, syncing to database", userXps.size());
        xpDatabaseService.setUserXpBatch(userXps);
        log.info("XP data sync completed successfully");
        xpRedisService.clearDirtyUsers();
    }
}
