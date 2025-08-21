package com.liwanag.gamification.repository;

import com.liwanag.gamification.model.UserComboStreak;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface UserComboStreakRepository extends JpaRepository<UserComboStreak, UUID> {
    @Query("SELECT x FROM UserComboStreak x ORDER BY x.maxStreak DESC")
    List<UserComboStreak> findTopUsersByMaxComboStreak(Pageable pageable);
}
