package com.liwanag.gamification.repository;

import com.liwanag.gamification.model.UserStats;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface UserStatsRepository extends JpaRepository<UserStats, UUID> {
    @Query("SELECT x FROM UserStats x ORDER BY x.correctCount DESC")
    List<UserStats> findTopUsersByCorrect(Pageable pageable);
}
