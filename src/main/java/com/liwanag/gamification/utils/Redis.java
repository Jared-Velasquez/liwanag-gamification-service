package com.liwanag.gamification.utils;

import java.util.UUID;

public class Redis {
    public static String generateUserXpKey(UUID userId) {
        return "user:" + userId + ":xp";
    }
}
