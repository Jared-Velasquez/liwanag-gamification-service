package com.liwanag.gamification.service.experience;

import com.liwanag.gamification.dto.experience.GetExperienceAndLevelResponse;
import com.liwanag.gamification.model.UserExperience;
import com.liwanag.gamification.repository.UserExperienceRepository;
import com.liwanag.gamification.utils.RedisKeys;
import com.liwanag.gamification.clients.RedisClient;
import com.liwanag.gamification.utils.TimeConstants;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExperienceService {
    private final UserExperienceRepository repository;
    private final RedisClient redisClient;
    private final Double TUNING_CONSTANT = 50.0;

    @Transactional
    public void incrementExperience(UUID userId, Integer deltaExperience) {
        // First increment experience in the database
        log.info("Incrementing experience for user {} by {}", userId, deltaExperience);

        repository.findById(userId).ifPresentOrElse(
                userExperience -> {
                    userExperience.setExperience(userExperience.getExperience() + deltaExperience);
                    userExperience.setLastUpdated(new Date());
                    repository.save(userExperience);
                },
                () -> repository.save(new UserExperience(userId, deltaExperience, new Date()))
        );

        // Then invalidate Redis cache
        String experienceKey = RedisKeys.getExperienceKey(userId);
        redisClient.invalidateKey(experienceKey);
    }

    public GetExperienceAndLevelResponse getExperienceAndLevel(UUID userId) {
        // First fetch experience points from Redis; if not present in Redis, fetch
        // from database

        String experienceKey = RedisKeys.getExperienceKey(userId);
        String experience = redisClient.vGet(experienceKey).orElse(null);

        if (experience != null) {
            return new GetExperienceAndLevelResponse(Integer.valueOf(experience), calculateLevelFromExperience(Integer.valueOf(experience)));
        }

        // Else query from database
        UserExperience data = repository.findById(userId).orElseGet(
                () -> repository.save(new UserExperience(userId, 0, new Date()))
        );

        // Then hydrate Redis
        redisClient.vSet(experienceKey, data.getExperience().toString(), TimeConstants.TEN_MINUTES);

        return new GetExperienceAndLevelResponse(data.getExperience(), calculateLevelFromExperience(data.getExperience()));
    }

    // TODO: redundant code from getExperienceAndLevel?
    public Integer getExperience(UUID userId) {
        // First fetch experience points from Redis; if not present in Redis, fetch
        // from Database

        String experienceKey = RedisKeys.getExperienceKey(userId);
        String experience = redisClient.vGet(experienceKey).orElse(null);

        if (experience != null) {
            return Integer.valueOf(experience);
        }

        // Else query from database
        UserExperience data = repository.findById(userId).orElseGet(
                () -> repository.save(new UserExperience(userId, 0, new Date()))
        );

        // Then hydrate Redis
        redisClient.vSet(experienceKey, data.getExperience().toString(), TimeConstants.TEN_MINUTES);

        return data.getExperience();
    }

    public Integer calculateLevelFromExperience(Integer experience) {
        if (experience < 0) {
            throw new IllegalArgumentException("Experience cannot be negative.");
        }

        return (int) Math.sqrt(experience / TUNING_CONSTANT);
    }

    public boolean hasLeveledUp(Integer baseExperience, Integer deltaExperience) {
        return calculateLevelFromExperience(baseExperience + deltaExperience) > calculateLevelFromExperience(baseExperience);
    }
}
