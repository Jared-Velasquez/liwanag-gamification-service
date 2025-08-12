package com.liwanag.gamification.service.streak;

import com.liwanag.gamification.dto.event.AnswerEvaluatedEvent;
import com.liwanag.gamification.dto.event.AnswerEvaluatedEvent.Result;
import com.liwanag.gamification.repository.UserComboStreakRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ComboStreakService {
    private final UserComboStreakRepository repository;

    private void incrementComboStreak(UUID userId) {

    }

    private void removeComboStreak(UUID userId) {

    }


    public void updateComboStreak(AnswerEvaluatedEvent event) {
        switch (event.getEnumResult()) {
            case Result.CORRECT:
                incrementComboStreak(event.getUserId());
                break;
            case Result.INCORRECT:
                removeComboStreak(event.getUserId());
                break;
            default:
                log.error("Event parameter in updateComboStreak is invalid: {}", event.getResult());
        }
    }
}
