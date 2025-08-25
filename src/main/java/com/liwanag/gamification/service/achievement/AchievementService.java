package com.liwanag.gamification.service.achievement;

import com.liwanag.gamification.dto.event.ActivityCompletedEvent;
import com.liwanag.gamification.dto.event.EpisodeCompletedEvent;
import com.liwanag.gamification.dto.event.UnitCompletedEvent;
import com.liwanag.gamification.model.UserStats;
import com.liwanag.gamification.repository.UserStatsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AchievementService {
    private final UserStatsRepository repository;
    // Achievements should be connected to the pedagogical side of Liwanag; incentivize the user by repeating
    // content to build upon foundations

    public void updateActivityCompleted(ActivityCompletedEvent event) {
        if (!event.getIsFirstCompletion()) {
            log.info("User has already completed activity {}", event.getActivityId());
            return;
        }

        UUID userId = event.getUserId();
        log.info("Incrementing activityCount for user {}", userId);

        repository.findById(userId).ifPresentOrElse(
                userStats -> {
                    userStats.setActivityCompletedCount(userStats.getActivityCompletedCount() + 1);
                    repository.save(userStats);
                },
                () -> repository.save(new UserStats(userId, 0, 0, 1, 0 ,0))
        );
    }

    public void updateEpisodeCompleted(EpisodeCompletedEvent event) {
        if (!event.getIsFirstCompletion()) {
            log.info("User has already completed episode {}", event.getEpisodeId());
            return;
        }

        UUID userId = event.getUserId();
        log.info("Incrementing episodeCount for user {}", userId);

        repository.findById(userId).ifPresentOrElse(
                userStats -> {
                    userStats.setEpisodeCompletedCount(userStats.getEpisodeCompletedCount() + 1);
                    repository.save(userStats);
                },
                () -> repository.save(new UserStats(userId, 0, 0, 0, 1, 0))
        );
    }

    public void updateUnitCompleted(UnitCompletedEvent event) {
        if (!event.getIsFirstCompletion()) {
            log.info("User has already completed unit {}", event.getUnitId());
            return;
        }

        UUID userId = event.getUserId();
        log.info("Incrementing unitCount for user {}", userId);

        repository.findById(userId).ifPresentOrElse(
                userStats -> {
                    userStats.setUnitCompletedCount(userStats.getUnitCompletedCount() + 1);
                    repository.save(userStats);
                },
                () -> repository.save(new UserStats(userId, 0, 0, 0, 0, 1))
        );
    }

    public void checkAchievementsUnlocked(UUID userId) {

    }
}
