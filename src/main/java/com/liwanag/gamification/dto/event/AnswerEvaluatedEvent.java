package com.liwanag.gamification.dto.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AnswerEvaluatedEvent {
    public enum Result {
        CORRECT,
        INCORRECT
    }

    private UUID userId;
    private String questionId;
    private String activityId;
    private String episodeId;
    private String unitId;
    private String result;
    private Integer xpGained;
    private Instant timestamp;

    public Result getEnumResult() {
        return Result.valueOf(this.result.toUpperCase());
    }
}