package com.liwanag.gamification.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liwanag.gamification.dto.AnswerEvaluatedEvent;
import com.liwanag.gamification.dto.Event;
import com.liwanag.gamification.service.AchievementService;
import com.liwanag.gamification.service.LeaderboardService;
import com.liwanag.gamification.service.StreakService;
import com.liwanag.gamification.service.XpService.XpDatabaseService;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GamificationQueueConsumer {
    private final AchievementService achievementService;
    private final LeaderboardService leaderboardService;
    private final StreakService streakService;
    private final XpDatabaseService xpService;

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
        // xpService.updateXP(message);
    }
}
