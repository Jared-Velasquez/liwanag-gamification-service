package com.liwanag.gamification.dto.leaderboard;

import java.util.UUID;

public record GetTopLevelsResponse(UUID userId, Integer level) {
}
