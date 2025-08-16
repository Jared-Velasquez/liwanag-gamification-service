package com.liwanag.gamification.repository;

import com.liwanag.gamification.model.UserExperience;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserExperienceRepository extends JpaRepository<UserExperience, UUID> {
}
