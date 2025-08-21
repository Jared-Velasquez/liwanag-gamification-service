package com.liwanag.gamification.dto.leaderboard;

import java.util.UUID;

public record GetTopDailyStreaksResponse(UUID userId, Integer maxStreak) {
}
