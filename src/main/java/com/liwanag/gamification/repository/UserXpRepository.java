package com.liwanag.gamification.repository;

import com.liwanag.gamification.model.UserXp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserXpRepository extends JpaRepository<UserXp, UUID> {
}
