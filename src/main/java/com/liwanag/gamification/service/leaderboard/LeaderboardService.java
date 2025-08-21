package com.liwanag.gamification.service.leaderboard;

import com.liwanag.gamification.clients.RedisClient;
import com.liwanag.gamification.dto.leaderboard.GetTopComboStreaksResponse;
import com.liwanag.gamification.dto.leaderboard.GetTopDailyStreaksResponse;
import com.liwanag.gamification.dto.leaderboard.GetTopLevelsResponse;
import com.liwanag.gamification.model.UserComboStreak;
import com.liwanag.gamification.model.UserDailyStreak;
import com.liwanag.gamification.model.UserExperience;
import com.liwanag.gamification.repository.UserComboStreakRepository;
import com.liwanag.gamification.repository.UserDailyStreakRepository;
import com.liwanag.gamification.repository.UserExperienceRepository;
import com.liwanag.gamification.service.experience.ExperienceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j

// Leaderboard service implementation adapted from: https://systemdesign.one/leaderboard-system-design/
public class LeaderboardService {
    private final UserExperienceRepository experienceRepository;
    private final ExperienceService experienceService;
    private final UserComboStreakRepository comboStreakRepository;
    private final UserDailyStreakRepository dailyStreakRepository;

    private final RedisClient redisClient;

    public List<GetTopLevelsResponse> getTopLevels(Integer count, Integer page) {
        List<UserExperience> topLevels = experienceRepository.findTopUsersByExperience(PageRequest.of(page, count));
        List<GetTopLevelsResponse> response = new ArrayList<>();
        for (UserExperience item : topLevels) {
            response.add(new GetTopLevelsResponse(item.getUserId(), experienceService.calculateLevelFromExperience(item.getExperience())));
        }

        return response;
    }

    public List<GetTopComboStreaksResponse> getTopComboStreaks(Integer count, Integer page) {
        List<UserComboStreak> topComboStreaks = comboStreakRepository.findTopUsersByMaxComboStreak(PageRequest.of(page, count));
        List<GetTopComboStreaksResponse> response = new ArrayList<>();
        for (UserComboStreak item : topComboStreaks) {
            response.add(new GetTopComboStreaksResponse(item.getUserId(), item.getMaxStreak()));
        }

        return response;
    }

    public List<GetTopDailyStreaksResponse> getTopDailyStreaks(Integer count, Integer page) {
        List<UserDailyStreak> topDailyStreaks = dailyStreakRepository.findTopUsersByMaxDailyStreak(PageRequest.of(page, count));
        List<GetTopDailyStreaksResponse> response = new ArrayList<>();
        for (UserDailyStreak item : topDailyStreaks) {
            response.add(new GetTopDailyStreaksResponse(item.getUserId(), item.getMaxStreak()));
        }

        return response;
    }
}
