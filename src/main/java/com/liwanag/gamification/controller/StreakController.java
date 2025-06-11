package com.liwanag.gamification.controller;

import com.liwanag.gamification.service.streak.DailyStreakService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/streaks")
@RequiredArgsConstructor
public class StreakController {
    private final DailyStreakService dailyStreakService;

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public Integer getUserStreak(@PathVariable String userId) {
        return dailyStreakService.getUserDailyStreak(UUID.fromString(userId));
    }
}
