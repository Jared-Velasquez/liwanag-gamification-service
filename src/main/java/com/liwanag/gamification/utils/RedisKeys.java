package com.liwanag.gamification.utils;

import java.util.UUID;

public class RedisKeys {
    public static String getComboStreakKey(UUID userId) {
        return String.format("user:%s:combo", userId);
    }

    public static String getMaxComboStreakKey(UUID userId) {
        return String.format("user:%s:maxcombo", userId);
    }

    public static String getExperienceKey(UUID userId) {
        return String.format("user:%s:experience", userId);
    }

    public static String getQuestionStatsAttemptedKey(UUID userId) {
        return String.format("user:%s:questionstats:attempted", userId);
    }

    public static String getQuestionStatsCorrectKey(UUID userId) {
        return String.format("user:%s:questionstats:correct", userId);
    }
}
