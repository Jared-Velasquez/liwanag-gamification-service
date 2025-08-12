package com.liwanag.gamification.service.streak;

import com.liwanag.gamification.repository.UserComboStreakRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ComboStreakService {
    private final UserComboStreakRepository repository;
}
