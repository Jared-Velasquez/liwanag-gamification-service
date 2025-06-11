package com.liwanag.gamification.service.streak;

import com.liwanag.gamification.model.UserDailyStreak;
import com.liwanag.gamification.repository.UserDailyStreakRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class DailyStreakService {
    private final UserDailyStreakRepository repository;

    // TODO: don't update on every request, but on specific events
    public void updateUserDailyStreak(UUID userId) {
        UserDailyStreak streak = repository.findById(userId)
                .orElse(new UserDailyStreak(userId, 0, 0, null));

        LocalDate today = LocalDate.now();
        LocalDate lastActiveDate = streak.getLastActiveDate();

        if (lastActiveDate == null || lastActiveDate.isBefore(today.minusDays(1))) {
            log.info("User {} is starting a new streak", userId);
            streak.setStreak(1);
        } else if (lastActiveDate.isEqual(today.minusDays(1))) {
            log.info("User {} is continuing their streak; streak of day {}", userId, streak.getStreak() + 1);
            streak.setStreak(streak.getStreak() + 1);
        } else {
            streak.setStreak(1);
        }

        streak.setLastActiveDate(today);
        streak.setMaxStreak(Math.max(streak.getMaxStreak(), streak.getStreak()));
        repository.save(streak);
    }

    public Integer getUserDailyStreak(UUID userId) {
        log.info("Fetching daily streak for user {}", userId);
        return repository.findById(userId)
                .map(UserDailyStreak::getStreak)
                .orElse(0);
    }
}
