package com.liwanag.gamification.controller;

import com.liwanag.gamification.dto.experience.GetExperienceAndLevelResponse;
import com.liwanag.gamification.service.experience.ExperienceService;
import com.liwanag.gamification.utils.Validators;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/experience")
@RequiredArgsConstructor
public class ExperienceController {
    private final ExperienceService experienceService;

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public GetExperienceAndLevelResponse getExperienceAndLevel(@PathVariable String userId) {
        UUID id = Validators.convertToUUID(userId);
        if (id == null) {
            throw new IllegalArgumentException("User ID must be a valid UUID.");
        }
        return experienceService.getExperienceAndLevel(id);
    }
}
