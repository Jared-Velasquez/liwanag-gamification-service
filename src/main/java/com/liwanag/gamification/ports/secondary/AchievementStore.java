package com.liwanag.gamification.ports.secondary;

import com.liwanag.gamification.domain.achievement.Achievement;
import com.liwanag.gamification.domain.achievement.UserAchievement;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface AchievementStore {
    List<Achievement> loadAchievements();
    Set<UserAchievement> loadUserAchievements(UUID userId);
    void addAchievement(UserAchievement userAchievement);
}
