package com.liwanag.gamification.service.leaderboard;

import com.liwanag.gamification.clients.RedisClient;
import com.liwanag.gamification.dto.leaderboard.GetTopLevelsResponse;
import com.liwanag.gamification.model.UserComboStreak;
import com.liwanag.gamification.model.UserExperience;
import com.liwanag.gamification.repository.UserExperienceRepository;
import com.liwanag.gamification.service.experience.ExperienceService;
import com.liwanag.gamification.utils.KeyObjectPair;
import com.liwanag.gamification.utils.TimeConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j

// Leaderboard service implementation adapted from: https://systemdesign.one/leaderboard-system-design/
public class LeaderboardService {
    private final UserExperienceRepository userExperienceRepository;
    private final ExperienceService experienceService;
    private final RedisClient redisClient;

    public List<GetTopLevelsResponse> getTopLevels(Integer count, Integer page) {
        // Logic to fetch top levels

        List<UserExperience> topLevels = userExperienceRepository.findTopUsersByExperience(PageRequest.of(page, count));
        List<GetTopLevelsResponse> response = new ArrayList<>();
        for (UserExperience e : topLevels) {
            response.add(new GetTopLevelsResponse(e.getUserId(), experienceService.calculateLevelFromExperience(e.getExperience())));
        }

        return response;
    }

    // Example method to get top streaks
    public void getTopStreaks(int count) {
        log.info("Fetching top {} streaks", count);
        List<KeyObjectPair<UserComboStreak>> comboStreaks = new ArrayList<>();
        comboStreaks.add(new KeyObjectPair<>("jared1", new UserComboStreak(UUID.randomUUID(), 0, 0, new Date())));
        comboStreaks.add(new KeyObjectPair<>("jared2", new UserComboStreak(UUID.randomUUID(), 2, 3, new Date())));
        comboStreaks.add(new KeyObjectPair<>("jared3", new UserComboStreak(UUID.randomUUID(), 5, 5, new Date())));
        comboStreaks.add(new KeyObjectPair<>("jared4", new UserComboStreak(UUID.randomUUID(), 2, 4, new Date())));

        redisClient.hSetBulk(comboStreaks, TimeConstants.TEN_MINUTES);

        List<String> keys = new ArrayList<>();
        keys.add("jared1");
        keys.add("jared2");
        keys.add("jared3");
        keys.add("jared4");

        List<UserComboStreak> results = redisClient.hGetBulk(keys, UserComboStreak.class);

        for (UserComboStreak result : results) {
            if (result == null) {
                continue;
            }
            log.info("Result: {} {} {} {}", result.getUserId(), result.getStreak(), result.getMaxStreak(), result.getLastActiveDate());
        }
    }

    // Example method to get top answered questions
    public void getTopAnswered(int count) {
        log.info("Fetching top {} answered questions", count);
        // Logic to fetch top answered questions
    }
}
