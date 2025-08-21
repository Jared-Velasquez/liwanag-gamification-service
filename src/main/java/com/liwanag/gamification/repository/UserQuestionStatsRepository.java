package com.liwanag.gamification.repository;

import com.liwanag.gamification.model.UserQuestionStats;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface UserQuestionStatsRepository extends JpaRepository<UserQuestionStats, UUID> {
    @Query("SELECT x FROM UserQuestionStats x ORDER BY x.correctCount DESC")
    List<UserQuestionStats> findTopUsersByCorrect(Pageable pageable);
}
