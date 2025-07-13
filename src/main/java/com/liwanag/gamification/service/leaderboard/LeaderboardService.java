package com.liwanag.gamification.service.leaderboard;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LeaderboardService {
    public void getTopLevels(int count) {
        log.info("Fetching top {} levels", count);
        // Logic to fetch top levels
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
