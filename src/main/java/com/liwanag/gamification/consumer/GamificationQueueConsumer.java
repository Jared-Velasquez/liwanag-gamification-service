package com.liwanag.gamification.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liwanag.gamification.dto.event.*;
import com.liwanag.gamification.service.achievement.AchievementService;
import com.liwanag.gamification.service.experience.ExperienceService;
import com.liwanag.gamification.service.leaderboard.LeaderboardService;
import com.liwanag.gamification.service.questionstats.QuestionStatsService;
import com.liwanag.gamification.service.streaks.ComboStreakService;
import com.liwanag.gamification.service.streaks.DailyStreakService;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class GamificationQueueConsumer {
    private final ComboStreakService comboStreakService;
    private final DailyStreakService dailyStreakService;
    private final QuestionStatsService questionStatsService;
    private final ExperienceService experienceService;

    @SqsListener(value = "GamificationQueue")
    public void listen(String message) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Event envelope = mapper.readValue(message, new TypeReference<>() {});
        LiwanagEvent event = envelope.getDetail();


        switch (event.getEnumEventType()) {
            case ANSWER_EVALUATED -> handleAnswerEvaluated((AnswerEvaluatedEvent) event);
            case ACTIVITY_COMPLETED -> handleActivityCompleted((ActivityCompletedEvent) event);
            case EPISODE_COMPLETED -> handleEpisodeCompleted((EpisodeCompletedEvent) event);
            case UNIT_COMPLETED -> handleUnitCompleted((UnitCompletedEvent) event);
            default -> {
                log.warn("Received unknown event type: {}", event.getEnumEventType());
                return;
            }
        }
    }

    public void handleAnswerEvaluated(AnswerEvaluatedEvent event) {
        UUID userId = event.getUserId();

        // Process the message and call the appropriate service methods
        // achievementService.processMessage(message);
        // streakService.updateStreak(message);
        comboStreakService.updateComboStreak(event);
        dailyStreakService.updateUserDailyStreak(userId);
        questionStatsService.recordAnswer(event);


        // Handle experience points
        if (event.getXpGained() != null) {
            Integer baseExperience = experienceService.getExperience(userId);
            Integer deltaExperience = event.getXpGained();
            experienceService.incrementExperience(userId, deltaExperience);

            if (experienceService.hasLeveledUp(baseExperience, deltaExperience)) {
                // TODO: emit event to NotificationService
                Integer baseLevel = experienceService.calculateLevelFromExperience(baseExperience);
                Integer newLevel = experienceService.calculateLevelFromExperience(baseExperience + deltaExperience);
                log.info("User has leveled up from {} to {}", baseLevel, newLevel);
            }
        }
    }

    public void handleActivityCompleted(ActivityCompletedEvent event) {

    }

    public void handleEpisodeCompleted(EpisodeCompletedEvent event) {

    }

    public void handleUnitCompleted(UnitCompletedEvent event) {

    }
}
