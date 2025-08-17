package com.liwanag.gamification.repository;

import com.liwanag.gamification.model.UserExperience;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface UserExperienceRepository extends JpaRepository<UserExperience, UUID> {
    @Query("SELECT x FROM UserExperience x ORDER BY x.experience DESC")
    List<UserExperience> findTopUsersByExperience(Pageable pageable);
}
