package com.liwanag.gamification.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liwanag.gamification.dto.event.AnswerEvaluatedEvent;
import com.liwanag.gamification.dto.event.Event;
import com.liwanag.gamification.service.achievement.AchievementService;
import com.liwanag.gamification.service.leaderboard.LeaderboardService;
import com.liwanag.gamification.service.level.LevelService;
import com.liwanag.gamification.service.streak.StreakService;
import com.liwanag.gamification.service.xp.XpService;
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
    private final StreakService streakService;
    private final XpService xpService;
    private final LevelService levelService;

    @SqsListener(value = "GamificationQueue")
    public void listen(String message) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Event<AnswerEvaluatedEvent> envelope = mapper.readValue(message, new TypeReference<Event<AnswerEvaluatedEvent>>() {});
        AnswerEvaluatedEvent event = envelope.getDetail();
        System.out.println("Received from SQS:");
        System.out.println(event.getQuestionId());
        System.out.println(event.getResult());
        System.out.println(event.getUserId());


        // Process the message and call the appropriate service methods
        // achievementService.processMessage(message);
        // leaderboardService.updateLeaderboard(message);
        // streakService.updateStreak(message);

        // Handle experience points
        Integer currentXp = xpService.getUserXp(UUID.fromString(event.getUserId()));
        xpService.incrementUserXp(UUID.fromString(event.getUserId()), event.getXpGained());
        Integer newXp = xpService.getUserXp(UUID.fromString(event.getUserId()));

        if (currentXp != null && newXp != null && levelService.checkLevelUp(currentXp, newXp)) {
            Integer newLevel = levelService.checkUserLevel(UUID.fromString(event.getUserId()));
            log.info("User {} leveled up to level {}", event.getUserId(), newLevel);
        }
    }
}
