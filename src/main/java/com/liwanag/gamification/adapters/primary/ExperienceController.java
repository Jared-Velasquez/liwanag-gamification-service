package com.liwanag.gamification.adapters.primary;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/experience")
@RequiredArgsConstructor
public final class ExperienceController {
    @GetMapping("/")
    public ResponseEntity<?> getExperience(
            @RequestHeader(name = "x-cognito-sub") UUID userId
    ) {
        return ResponseEntity.noContent().build();
    }
}
