package com.liwanag.gamification.service.questionstats;

import com.liwanag.gamification.clients.RedisClient;
import com.liwanag.gamification.dto.event.AnswerEvaluatedEvent;
import com.liwanag.gamification.dto.questionstats.GetQuestionStatsResponse;
import com.liwanag.gamification.model.UserStats;
import com.liwanag.gamification.repository.UserStatsRepository;
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
    private final UserStatsRepository repository;
    private final RedisClient redisClient;

    public GetQuestionStatsResponse getUserQuestionStatistics(UUID userId) {
        String userStatsKey = RedisKeys.getQuestionStatsKey(userId);
        GetQuestionStatsResponse cachedStats = redisClient.objGet(userStatsKey, GetQuestionStatsResponse.class).orElse(null);

        if (cachedStats != null) {
            return cachedStats;
        }

        UserStats stats = repository.findById(userId).orElse(new UserStats(userId, 0, 0, 0, 0, 0));
        GetQuestionStatsResponse response = new GetQuestionStatsResponse(stats.getAttemptedCount(), stats.getCorrectCount(), stats.getActivityCompletedCount(), stats.getEpisodeCompletedCount(), stats.getUnitCompletedCount());

        // Cache the stats in Redis for future requests
        redisClient.objSet(userStatsKey, response, TimeConstants.TEN_MINUTES);
        return response;
    }

    @Transactional
    public void recordAnswer(AnswerEvaluatedEvent event) {
        UUID userId = event.getUserId();

        repository.findById(userId).map(stat -> {
            stat.setAttemptedCount(stat.getAttemptedCount() + 1);
            if (event.getEnumResult() == Result.CORRECT)
                stat.setCorrectCount(stat.getCorrectCount() + 1);
            return repository.save(stat);
        }).orElseGet(() -> repository.save(new UserStats(userId, 1, event.getEnumResult() == Result.CORRECT ? 1 : 0, 0, 0, 0)));
    }
}
