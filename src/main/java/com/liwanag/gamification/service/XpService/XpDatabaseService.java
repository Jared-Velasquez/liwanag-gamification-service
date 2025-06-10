package com.liwanag.gamification.service.XpService;

import com.liwanag.gamification.model.UserXp;
import com.liwanag.gamification.repository.UserXpRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class XpDatabaseService {
    private final UserXpRepository repository;

    @Transactional
    public void saveUserXp(UUID userId, Integer xp, Instant timestamp) {
        repository.findById(userId).ifPresentOrElse(
            userXp -> {
                userXp.setXp(userXp.getXp() + xp);
                userXp.setLastUpdated(timestamp);
                repository.save(userXp);
            },
            () -> repository.save(new UserXp(userId, xp, timestamp))
        );
    }

    @Transactional
    public Integer getUserXp(UUID userId) {
        if (repository.existsById(userId)) {
            return repository.findById(userId)
                .map(UserXp::getXp)
                .orElse(0);
        }

        // Create a new UserXp entry with 0 XP if it doesn't exist
        UserXp newUserXp = new UserXp(userId, 0, Instant.now());
        repository.save(newUserXp);
        return 0;
    }

    public boolean isDatabaseUp() {
        try {
            return repository.count() >= 0; // Just a simple check to see if the database is accessible
        } catch (Exception e) {
            return false; // If any exception occurs, we assume the database is down
        }
    }
}
