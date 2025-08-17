package com.liwanag.gamification.service.leaderboard;

import com.liwanag.gamification.model.UserExperience;
import com.liwanag.gamification.repository.UserExperienceRepository;
import com.liwanag.gamification.service.experience.ExperienceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j

// Leaderboard service implementation adapted from: https://systemdesign.one/leaderboard-system-design/
public class LeaderboardService {
    private final UserExperienceRepository userExperienceRepository;

    private final ExperienceService experienceService;
    public void getTopLevels(Integer count, Integer page) {
        log.info("Fetching top {} levels on page {}", count, page);
        // Logic to fetch top levels

        List<UserExperience> topLevels = userExperienceRepository.findTopUsersByExperience(PageRequest.of(page, count));

        for (UserExperience level : topLevels) {
            log.info("Level {} for user {}", experienceService.calculateLevelFromExperience(level.getExperience()), level.getUserId());
        }
    }

    // Example method to get top streaks
    public void getTopStreaks(int count) {
        log.info("Fetching top {} streaks", count);
        // Logic to fetch top streaks
    }

    // Example method to get top answered questions
    public void getTopAnswered(int count) {
        log.info("Fetching top {} answered questions", count);
        // Logic to fetch top answered questions
    }
}
