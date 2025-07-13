package com.liwanag.gamification.controller;

import com.liwanag.gamification.service.leaderboard.LeaderboardService;
import com.liwanag.gamification.utils.Validators;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/leaderboards")
@RequiredArgsConstructor
public class LeaderboardController {
    private final LeaderboardService leaderboardService;

    @GetMapping("/top-levels/{count}")
    @ResponseStatus(HttpStatus.OK)
    public void getTopLevels(@PathVariable String count) {
        Integer numUsers = Validators.convertToInteger(count);
        if (numUsers == null || numUsers <= 0) {
            throw new IllegalArgumentException("Count must be a valid positive integer.");
        }
        leaderboardService.getTopLevels(numUsers);
    }

    @GetMapping("/top-streaks/{count}")
    @ResponseStatus(HttpStatus.OK)
    public void getTopStreaks(@PathVariable String count) {
        Integer numUsers = Validators.convertToInteger(count);
        if (numUsers == null || numUsers <= 0) {
            throw new IllegalArgumentException("Count must be a valid positive integer.");
        }
        leaderboardService.getTopStreaks(numUsers);
    }

    @GetMapping("/top-answered/{count}")
    @ResponseStatus(HttpStatus.OK)
    public void getTopAnswered(@PathVariable String count) {
        Integer numUsers = Validators.convertToInteger(count);
        if (numUsers == null || numUsers <= 0) {
            throw new IllegalArgumentException("Count must be a valid positive integer.");
        }
        leaderboardService.getTopAnswered(numUsers);
    }
}
