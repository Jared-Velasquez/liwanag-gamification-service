package com.liwanag.gamification.repository;

import com.liwanag.gamification.model.UserDailyStreak;
import com.liwanag.gamification.model.UserExperience;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface UserDailyStreakRepository extends JpaRepository<UserDailyStreak, UUID> {
    @Query("SELECT x FROM UserDailyStreak x ORDER BY x.maxStreak DESC")
    List<UserDailyStreak> findTopUsersByMaxDailyStreak(Pageable pageable);
}
