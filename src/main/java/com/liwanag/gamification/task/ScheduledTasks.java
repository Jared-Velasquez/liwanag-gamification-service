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
        // TODO: only sync dirty/modified XP entries, batch writes, add error handling, perform distributed locking
        log.info("Syncing XP data from Redis to the database");
        List<UserXp> userXps = xpRedisService.getAllUserXps();
        userXps.forEach(userXp -> {
            xpDatabaseService.setUserXp(userXp.getUserId(), userXp.getXp(), userXp.getLastUpdated());
        });
    }
}
