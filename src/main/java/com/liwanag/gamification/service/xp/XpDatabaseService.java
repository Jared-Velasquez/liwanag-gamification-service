package com.liwanag.gamification.service.xp;

import com.liwanag.gamification.model.UserXp;
import com.liwanag.gamification.repository.UserXpRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class XpDatabaseService {
    private final UserXpRepository repository;

    @Transactional
    public void incrementUserXp(UUID userId, Integer xp, Instant timestamp) {
        log.info("Incrementing XP in database for user {} by {}", userId, xp);
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
        log.info("Fetching XP from database for user {}", userId);
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

//    @Transactional
//    public void setUserXp(UUID userId, Integer xp, Instant timestamp) {
//        log.info("Setting XP in database for user {} to {}", userId, xp);
//        if (repository.existsById(userId)) {
//            UserXp existingUserXp = repository.findById(userId).orElseThrow();
//            existingUserXp.setXp(xp);
//            existingUserXp.setLastUpdated(timestamp);
//            repository.save(existingUserXp);
//            return;
//        }
//
//        UserXp userXp = new UserXp(userId, xp, timestamp);
//        repository.save(userXp);
//    }

    @Transactional
    public void setUserXp(UserXp userXp) {
        log.info("Setting XP in database for user {} to {}", userXp.getUserId(), userXp.getXp());
        if (repository.existsById(userXp.getUserId())) {
            UserXp existingUserXp = repository.findById(userXp.getUserId()).orElseThrow();
            existingUserXp.setXp(userXp.getXp());
            existingUserXp.setLastUpdated(userXp.getLastUpdated());
            repository.save(existingUserXp);
        } else {
            repository.save(userXp);
        }
    }

    @Transactional
    public void setUserXpBatch(List<UserXp> userXps) {
        repository.saveAll(userXps);
    }

    public boolean isDatabaseUp() {
        try {
            return repository.count() >= 0; // Just a simple check to see if the database is accessible
        } catch (Exception e) {
            return false; // If any exception occurs, we assume the database is down
        }
    }
}
