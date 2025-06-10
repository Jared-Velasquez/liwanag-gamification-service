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
}
