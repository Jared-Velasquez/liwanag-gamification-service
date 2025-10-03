package com.liwanag.gamification.adapters.primary;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/leaderboards")
@RequiredArgsConstructor
public final class LeaderboardController {
    @GetMapping("/levels")
    public ResponseEntity<?> getTopLevels(
            @RequestParam(value = "count", defaultValue = "5") @Min(1) int count,
            @RequestParam(value = "page", defaultValue = "0") @Min(0) int page
    ) {
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/combo")
    public ResponseEntity<?> getTopComboStreaks(
            @RequestParam(value = "count", defaultValue = "5") @Min(1) int count,
            @RequestParam(value = "page", defaultValue = "0") @Min(0) int page
    ) {
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/daily")
    public ResponseEntity<?> getTopDailyPoints(
            @RequestParam(value = "count", defaultValue = "5") @Min(1) int count,
            @RequestParam(value = "page", defaultValue = "0") @Min(0) int page
    ) {
        return ResponseEntity.noContent().build();
    }
}
