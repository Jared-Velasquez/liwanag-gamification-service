package com.liwanag.gamification.dto.questionstats;

public record GetQuestionStatsResponse(Integer attemptedCount, Integer correctCount, Integer activityCompletedCount, Integer episodeCompletedCount, Integer unitCompletedCount) {
}