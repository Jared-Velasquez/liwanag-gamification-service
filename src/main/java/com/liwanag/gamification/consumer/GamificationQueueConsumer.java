package com.liwanag.gamification.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liwanag.gamification.dto.event.*;
import com.liwanag.gamification.service.achievement.AchievementService;
import com.liwanag.gamification.service.experience.ExperienceService;
import com.liwanag.gamification.service.leaderboard.LeaderboardService;
import com.liwanag.gamification.service.questionstats.QuestionStatsService;
import com.liwanag.gamification.service.streaks.ComboStreakService;
import com.liwanag.gamification.service.streaks.DailyStreakService;
import com.liwanag.gamification.dto.event.LiwanagEvent.EventType;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class GamificationQueueConsumer {
    private final ComboStreakService comboStreakService;
    private final DailyStreakService dailyStreakService;
    private final QuestionStatsService questionStatsService;
    private final ExperienceService experienceService;
    private final AchievementService achievementService;
    private final ObjectMapper objectMapper;

    @SqsListener(value = "GamificationQueue")
    public void listen(String message) throws JsonProcessingException {
        EventType eventType = extractEventType(message).orElse(null);

        if (eventType == null)
            return;

        JsonNode root = objectMapper.readTree(message);
        JsonNode detailNode = root.path("detail");

        switch (eventType) {
            case EventType.AnswerEvaluated -> {
                AnswerEvaluatedEvent event = objectMapper.treeToValue(detailNode, AnswerEvaluatedEvent.class);
                handleAnswerEvaluated(event);
            }
            case EventType.ActivityCompleted -> {
                ActivityCompletedEvent event = objectMapper.treeToValue(detailNode, ActivityCompletedEvent.class);
                handleActivityCompleted(event);
            }
            case EventType.EpisodeCompleted -> {
                EpisodeCompletedEvent event = objectMapper.treeToValue(detailNode, EpisodeCompletedEvent.class);
                handleEpisodeCompleted(event);
            }
            case EventType.UnitCompleted -> {
                UnitCompletedEvent event = objectMapper.treeToValue(detailNode, UnitCompletedEvent.class);
                handleUnitCompleted(event);
            }
            default -> log.warn("Unknown eventType: {}", eventType);
        }
    }

    private Optional<EventType> extractEventType(String message) {
        try {
            JsonNode root = objectMapper.readTree(message);
            String detailType = root.path("detail-type").asText(null);
            JsonNode detailNode = root.path("detail");

            if (detailType == null || detailNode.isMissingNode()) {
                log.warn("Received event without detailType or detail: {}", message);
                return Optional.empty();
            }

            return Optional.ofNullable(LiwanagEvent.getEnumEventType(detailType));
        } catch (Exception e) {
            log.error("Exception occurred when processing message {}", message, e);
            return Optional.empty();
        }
    }

    public void handleAnswerEvaluated(AnswerEvaluatedEvent event) {
        log.info("Handling AnswerEvaluated event");

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
        log.info("Handling ActivityCompleted event");
        UUID userId = event.getUserId();

        achievementService.updateActivityCompleted(event);
        achievementService.checkAchievementsUnlocked(userId);
    }

    public void handleEpisodeCompleted(EpisodeCompletedEvent event) {
        log.info("Handling EpisodeCompleted event");
        UUID userId = event.getUserId();

        achievementService.updateEpisodeCompleted(event);
        achievementService.checkAchievementsUnlocked(userId);
    }

    public void handleUnitCompleted(UnitCompletedEvent event) {
        log.info("Handling UnitCompleted event");
        UUID userId = event.getUserId();

        achievementService.updateUnitCompleted(event);
        achievementService.checkAchievementsUnlocked(userId);
    }
}
