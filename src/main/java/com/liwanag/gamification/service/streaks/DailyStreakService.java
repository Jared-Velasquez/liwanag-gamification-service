package com.liwanag.gamification.service.streaks;

import com.liwanag.gamification.dto.streaks.GetDailyStreaksResponse;
import com.liwanag.gamification.model.UserDailyStreak;
import com.liwanag.gamification.repository.UserDailyStreakRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
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

        Date today = new Date();
        Date lastActiveDate = streak.getLastActiveDate();

//        if (lastActiveDate == null || lastActiveDate.isBefore(today.minusDays(1))) {
//            log.info("User {} is starting a new streak", userId);
//            streak.setStreak(1);
//        } else if (lastActiveDate.isEqual(today.minusDays(1))) {
//            log.info("User {} is continuing their streak; streak of day {}", userId, streak.getStreak() + 1);
//            streak.setStreak(streak.getStreak() + 1);
//        } else {
//            streak.setStreak(1);
//        }

        streak.setLastActiveDate(today);
        streak.setMaxStreak(Math.max(streak.getMaxStreak(), streak.getStreak()));
        repository.save(streak);
    }

    // TODO: make resetting into a scheduled job? If the user has a high streak and then becomes inactive, the
    // streak won't reset with this code.
    @Transactional
    public GetDailyStreaksResponse getDailyStreaks(UUID userId) {
        log.info("Fetching daily streak for user {}", userId);
        // If the user's streak is queried and the last active date is not yesterday or today,
        // reset the streak
        UserDailyStreak userDailyStreak = repository.findById(userId).orElse(null);

        if (userDailyStreak != null) {
            Date lastActiveDate = userDailyStreak.getLastActiveDate();
            Date today = new Date();

//            if (lastActiveDate == null || !lastActiveDate.isEqual(today) && !lastActiveDate.isEqual(today.minusDays(1))) {
//                log.info("Resetting streak for user {} due to inactivity", userId);
//                userDailyStreak.setStreak(0);
//                userDailyStreak.setLastActiveDate(today);
//                repository.save(userDailyStreak);
//            }

            return new GetDailyStreaksResponse(userDailyStreak.getStreak(), userDailyStreak.getMaxStreak());
        }

        log.info("No streak found for user {}, returning 0", userId);
        return new GetDailyStreaksResponse(0, 0);
    }
}
