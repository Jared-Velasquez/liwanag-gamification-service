package com.liwanag.gamification.service.questionstats;

import com.liwanag.gamification.clients.RedisClient;
import com.liwanag.gamification.dto.event.AnswerEvaluatedEvent;
import com.liwanag.gamification.model.UserQuestionStats;
import com.liwanag.gamification.repository.UserQuestionStatsRepository;
import com.liwanag.gamification.dto.event.AnswerEvaluatedEvent.Result;
import com.liwanag.gamification.utils.RedisKeys;
import com.liwanag.gamification.utils.TimeConstants;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuestionStatsService {
    private final UserQuestionStatsRepository repository;
    private final RedisClient redisClient;

    @Transactional
    public void recordAnswer(AnswerEvaluatedEvent event) {
        UUID userId = event.getUserId();
        UserQuestionStats updated = repository.findById(userId).map(stat -> {
            stat.setAttemptedCount(stat.getAttemptedCount() + 1);
            if (event.getEnumResult() == Result.CORRECT)
                stat.setCorrectCount(stat.getCorrectCount() + 1);
            return repository.save(stat);
        }).orElseGet(() -> repository.save(new UserQuestionStats(userId, 1, event.getEnumResult() == Result.CORRECT ? 1 : 0)));

        String statAttemptedKey = RedisKeys.getQuestionStatsAttemptedKey(userId);
        String statCorrectKey = RedisKeys.getQuestionStatsCorrectKey(userId);
        redisClient.vSet(statAttemptedKey, updated.getAttemptedCount().toString(), TimeConstants.TEN_MINUTES);
        redisClient.vSet(statCorrectKey, updated.getCorrectCount().toString(), TimeConstants.TEN_MINUTES);
    }
}
