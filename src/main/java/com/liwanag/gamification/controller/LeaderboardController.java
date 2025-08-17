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

    @GetMapping("/levels")
    @ResponseStatus(HttpStatus.OK)
    public void getTopLevels(@RequestParam(value = "count", defaultValue = "5") Integer count, @RequestParam(value = "page", defaultValue = "0") Integer page) {
        if (count <= 0) {
            throw new IllegalArgumentException("Count must be a positive integer.");
        }

        if (page < 0) {
            throw new IllegalArgumentException("Page must be a non-negative integer.");
        }

        leaderboardService.getTopLevels(count, page);
    }

    @GetMapping("/streaks/{count}")
    @ResponseStatus(HttpStatus.OK)
    public void getTopStreaks(@PathVariable String count) {
        Integer numUsers = Validators.convertToInteger(count);
        if (numUsers == null || numUsers <= 0) {
            throw new IllegalArgumentException("Count must be a valid positive integer.");
        }
        leaderboardService.getTopStreaks(numUsers);
    }

    @GetMapping("/answered/{count}")
    @ResponseStatus(HttpStatus.OK)
    public void getTopAnswered(@PathVariable String count) {
        Integer numUsers = Validators.convertToInteger(count);
        if (numUsers == null || numUsers <= 0) {
            throw new IllegalArgumentException("Count must be a valid positive integer.");
        }
        leaderboardService.getTopAnswered(numUsers);
    }
}
