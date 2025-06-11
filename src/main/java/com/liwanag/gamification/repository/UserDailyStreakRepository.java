package com.liwanag.gamification.repository;

import com.liwanag.gamification.model.UserDailyStreak;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserDailyStreakRepository extends JpaRepository<UserDailyStreak, UUID> {
}
