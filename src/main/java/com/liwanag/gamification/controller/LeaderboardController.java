package com.liwanag.gamification.controller;

import com.liwanag.gamification.dto.leaderboard.GetTopComboStreaksResponse;
import com.liwanag.gamification.dto.leaderboard.GetTopDailyStreaksResponse;
import com.liwanag.gamification.dto.leaderboard.GetTopLevelsResponse;
import com.liwanag.gamification.service.leaderboard.LeaderboardService;
import com.liwanag.gamification.utils.Validators;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leaderboards")
@RequiredArgsConstructor
public class LeaderboardController {
    private final LeaderboardService leaderboardService;

    @GetMapping("/levels")
    @ResponseStatus(HttpStatus.OK)
    public List<GetTopLevelsResponse> getTopLevels(
            @RequestParam(value = "count", defaultValue = "5") Integer count,
            @RequestParam(value = "page", defaultValue = "0") Integer page
    ) {
        if (count <= 0) {
            throw new IllegalArgumentException("Count must be a positive integer.");
        }

        if (page < 0) {
            throw new IllegalArgumentException("Page must be a non-negative integer.");
        }

        return leaderboardService.getTopLevels(count, page);
    }

    @GetMapping("/combo")
    @ResponseStatus(HttpStatus.OK)
    public List<GetTopComboStreaksResponse> getTopComboStreaks(
            @RequestParam(value = "count", defaultValue = "5") Integer count,
            @RequestParam(value = "page", defaultValue = "0") Integer page
    ) {
        if (count <= 0) {
            throw new IllegalArgumentException("Count must be a positive integer.");
        }

        if (page < 0) {
            throw new IllegalArgumentException("Page must be a non-negative integer.");
        }

        return leaderboardService.getTopComboStreaks(count, page);
    }

    @GetMapping("/daily")
    @ResponseStatus(HttpStatus.OK)
    public List<GetTopDailyStreaksResponse> getTopDailyStreaks(
            @RequestParam(value = "count", defaultValue = "5") Integer count,
            @RequestParam(value = "page", defaultValue = "0") Integer page
    ) {
        if (count <= 0) {
            throw new IllegalArgumentException("Count must be a positive integer.");
        }

        if (page < 0) {
            throw new IllegalArgumentException("Page must be a non-negative integer.");
        }

        return leaderboardService.getTopDailyStreaks(count, page);
    }

//    @GetMapping("/answered")
//    @ResponseStatus(HttpStatus.OK)
//    public void getTopAnswered(
//            @RequestParam(value = "count", defaultValue = "5") Integer count,
//            @RequestParam(value = "page", defaultValue = "0") Integer page
//    ) {
//        if (count <= 0) {
//            throw new IllegalArgumentException("Count must be a positive integer.");
//        }
//
//        if (page < 0) {
//            throw new IllegalArgumentException("Page must be a non-negative integer.");
//        }
//
//        return leaderboardService.getTopAnswered(numUsers);
//    }
}
