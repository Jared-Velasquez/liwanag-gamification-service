package com.liwanag.gamification.dto.leaderboard;

import java.util.UUID;

public record GetTopCorrectResponse(UUID userId, Integer correctCount) {
}
