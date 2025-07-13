package com.liwanag.gamification.controller;

import com.liwanag.gamification.service.level.LevelService;
import com.liwanag.gamification.service.xp.XpService;
import com.liwanag.gamification.utils.Validators;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/xp")
@RequiredArgsConstructor
public class XpController {
    private final XpService xpService;
    private final LevelService levelService;

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public Integer getUserXp(@PathVariable String userId) {
        UUID id = Validators.convertToUUID(userId);
        if (id == null) {
            throw new IllegalArgumentException("User ID must be a valid UUID.");
        }
        return xpService.getUserXp(id);
    }

    @GetMapping("/level/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public Integer getUserLevel(@PathVariable String userId) {
        UUID id = Validators.convertToUUID(userId);
        if (id == null) {
            throw new IllegalArgumentException("User ID must be a valid UUID.");
        }
        return levelService.checkUserLevel(id);
    }
}
