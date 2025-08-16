package com.liwanag.gamification.controller;

import com.liwanag.gamification.dto.streaks.GetComboStreaksResponse;
import com.liwanag.gamification.service.streaks.ComboStreakService;
import com.liwanag.gamification.service.streaks.DailyStreakService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/streaks")
@RequiredArgsConstructor
public class StreakController {
    private final DailyStreakService dailyStreakService;
    private final ComboStreakService comboStreakService;

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public Integer getUserStreak(@PathVariable String userId) {
        return dailyStreakService.getUserDailyStreak(UUID.fromString(userId));
    }

    @GetMapping("/{userId}/combo")
    @ResponseStatus(HttpStatus.OK)
    public GetComboStreaksResponse getComboStreak(@PathVariable String userId) {
        return comboStreakService.getComboStreaks(UUID.fromString(userId));
    }
}
