package com.liwanag.gamification.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liwanag.gamification.dto.event.AnswerEvaluatedEvent;
import com.liwanag.gamification.dto.event.Event;
import com.liwanag.gamification.service.achievement.AchievementService;
import com.liwanag.gamification.service.experience.ExperienceService;
import com.liwanag.gamification.service.leaderboard.LeaderboardService;
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
    private final AchievementService achievementService;
    private final LeaderboardService leaderboardService;
    private final ComboStreakService comboStreakService;
    private final DailyStreakService dailyStreakService;
    private final ExperienceService experienceService;

    @SqsListener(value = "GamificationQueue")
    public void listen(String message) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Event<AnswerEvaluatedEvent> envelope = mapper.readValue(message, new TypeReference<Event<AnswerEvaluatedEvent>>() {});
        AnswerEvaluatedEvent event = envelope.getDetail();
        System.out.println("Received from SQS:");
        System.out.println(event.getQuestionId());
        System.out.println(event.getResult());
        System.out.println(event.getUserId());

        UUID userId = event.getUserId();


        // Process the message and call the appropriate service methods
        // achievementService.processMessage(message);
        // leaderboardService.updateLeaderboard(message);
        // streakService.updateStreak(message);
        comboStreakService.updateComboStreak(event);
        dailyStreakService.updateUserDailyStreak(userId);

        // Handle experience points
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
