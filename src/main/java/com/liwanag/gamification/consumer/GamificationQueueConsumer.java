package com.liwanag.gamification.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liwanag.gamification.dto.AnswerEvaluatedEvent;
import com.liwanag.gamification.service.AchievementService;
import com.liwanag.gamification.service.LeaderboardService;
import com.liwanag.gamification.service.StreakService;
import com.liwanag.gamification.service.XPService;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GamificationQueueConsumer {
    private final AchievementService achievementService;
    private final LeaderboardService leaderboardService;
    private final StreakService streakService;
    private final XPService xpService;

    @SqsListener(value = "GamificationQueue")
    public void listen(String message) throws JsonProcessingException {
        AnswerEvaluatedEvent event = new ObjectMapper().readValue(message, AnswerEvaluatedEvent.class);
        System.out.println("Received from SQS:");
        System.out.println(event.activityId());
        System.out.println(event.userId());
        System.out.println(event.episodeId());
        System.out.println(event.questionId());
        System.out.println(event.unitId());
        System.out.println(event.result());


        // Process the message and call the appropriate service methods
        // For example:
        // achievementService.processMessage(message);
        // leaderboardService.updateLeaderboard(message);
        // streakService.updateStreak(message);
        // xpService.updateXP(message);
    }
}
