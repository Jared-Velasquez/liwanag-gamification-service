package com.liwanag.gamification.controller;

import com.liwanag.gamification.service.xp.XpService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/xp")
@RequiredArgsConstructor
public class XpController {
    private final XpService xpService;

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public Integer getUserXp(@PathVariable String userId) {
        return xpService.getUserXp(UUID.fromString(userId));
    }
}
