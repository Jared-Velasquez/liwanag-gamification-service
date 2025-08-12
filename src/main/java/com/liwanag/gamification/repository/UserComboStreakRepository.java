package com.liwanag.gamification.repository;

import com.liwanag.gamification.model.UserComboStreak;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserComboStreakRepository extends JpaRepository<UserComboStreak, UUID> {
}
