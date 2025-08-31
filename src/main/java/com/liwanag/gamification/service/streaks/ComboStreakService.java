package com.liwanag.gamification.service.streaks;

import com.liwanag.gamification.clients.RedisClient;
import com.liwanag.gamification.dto.event.AnswerEvaluatedEvent;
import com.liwanag.gamification.dto.event.AnswerEvaluatedEvent.Result;
import com.liwanag.gamification.dto.streaks.GetComboStreaksResponse;
import com.liwanag.gamification.model.UserComboStreak;
import com.liwanag.gamification.repository.UserComboStreakRepository;
import com.liwanag.gamification.utils.RedisKeys;
import com.liwanag.gamification.utils.TimeConstants;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ComboStreakService {
    private final UserComboStreakRepository repository;
    private final RedisClient redisClient;

    @Transactional
    private void incrementComboStreak(UUID userId) {
        // First update combo streak in the database
        log.info("Incrementing combo streak in database for user {}", userId);
        UserComboStreak updated = repository.findById(userId).map(streak -> {
            Integer incremented = streak.getStreak() + 1;
            streak.setStreak(incremented);
            streak.setMaxStreak(Math.max(streak.getMaxStreak(), incremented));
            return repository.save(streak);
        }).orElseGet(() -> repository.save(new UserComboStreak(userId, 1, 1)));

        // Then update combo streak in Redis
        String streakKey = RedisKeys.getComboStreakKey(userId);
        String maxStreakKey = RedisKeys.getMaxComboStreakKey(userId);
        redisClient.vSet(streakKey, updated.getStreak().toString(), TimeConstants.TEN_MINUTES);
        redisClient.vSet(maxStreakKey, updated.getMaxStreak().toString(), TimeConstants.TEN_MINUTES);
    }

    @Transactional
    private void removeComboStreak(UUID userId) {
        log.info("Removing combo streak in database for user {}", userId);
        UserComboStreak updated = repository.findById(userId).map(streak -> {
            streak.setStreak(0);
            return repository.save(streak);
        }).orElseGet(() -> repository.save(new UserComboStreak(userId, 0, 0)));

        // Then update combo streak in Redis
        String key = RedisKeys.getComboStreakKey(userId);
        // updated.getStreak() would always return 0, I'm just keeping it for consistency
        redisClient.vSet(key, updated.getStreak().toString(), TimeConstants.TEN_MINUTES);
    }

    @Transactional
    public void updateComboStreak(AnswerEvaluatedEvent event) {
        switch (event.getEnumResult()) {
            case CORRECT:
                incrementComboStreak(event.getUserId());
                break;
            case INCORRECT:
                removeComboStreak(event.getUserId());
                break;
            default:
                log.error("Event parameter in updateComboStreak is invalid: {}", event.getResult());
        }
    }

    public GetComboStreaksResponse getComboStreaks(UUID userId) {
        // First fetch from Redis; if combo streak or max combo streak isn't present, then fetch
        // from database

        String streakKey = RedisKeys.getComboStreakKey(userId);
        String maxStreakKey = RedisKeys.getMaxComboStreakKey(userId);
        String streak = redisClient.vGet(streakKey).orElse(null);
        String maxStreak = redisClient.vGet(maxStreakKey).orElse(null);

        // If both streak and maxStreak were found, immediately return
        if (streak != null && maxStreak != null) {
            return new GetComboStreaksResponse(Integer.valueOf(streak), Integer.valueOf(maxStreak));
        }

        // Else query from database
        UserComboStreak data = repository.findById(userId).orElseGet(
                () -> repository.save(new UserComboStreak(userId, 0, 0))
        );

        // Then hydrate Redis
        redisClient.vSet(streakKey, data.getStreak().toString(), TimeConstants.TEN_MINUTES);
        redisClient.vSet(maxStreakKey, data.getMaxStreak().toString(), TimeConstants.TEN_MINUTES);

        return new GetComboStreaksResponse(data.getStreak(), data.getMaxStreak());
    }
}
