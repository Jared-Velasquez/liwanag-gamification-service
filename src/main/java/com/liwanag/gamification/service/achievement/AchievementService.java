package com.liwanag.gamification.service.achievement;

import com.liwanag.gamification.dto.event.ActivityCompletedEvent;
import com.liwanag.gamification.dto.event.EpisodeCompletedEvent;
import com.liwanag.gamification.dto.event.UnitCompletedEvent;
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

    }

    public void updateEpisodeCompleted(EpisodeCompletedEvent event) {

    }

    public void updateUnitCompleted(UnitCompletedEvent event) {

    }

    public void checkAchievementsUnlocked(UUID userId) {

    }
}
