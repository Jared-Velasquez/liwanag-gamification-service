package com.liwanag.gamification.controller;

import com.liwanag.gamification.dto.questionstats.GetQuestionStatsResponse;
import com.liwanag.gamification.service.questionstats.QuestionStatsService;
import com.liwanag.gamification.utils.Validators;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class StatisticsController {
    private final QuestionStatsService statisticsService;

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public GetQuestionStatsResponse getUserQuestionStatistics(@PathVariable String userId) {
        UUID id = Validators.convertToUUID(userId);
        if (id == null) {
            throw new IllegalArgumentException("User ID must be a valid UUID.");
        }
        return statisticsService.getUserQuestionStatistics(id);
    }
}
