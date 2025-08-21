package com.liwanag.gamification.dto.leaderboard;

import java.util.UUID;

public record GetTopComboStreaksResponse(UUID userId, Integer maxStreak) {
}
