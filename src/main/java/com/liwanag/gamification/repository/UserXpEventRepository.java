package com.liwanag.gamification.repository;

import com.liwanag.gamification.model.UserXpEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserXpEventRepository extends JpaRepository<UserXpEvent, UUID> {
}
