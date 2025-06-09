package com.liwanag.gamification.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@RequiredArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AnswerEvaluatedEvent {
    private String userId;
    private String questionId;
    private String activityId;
    private String episodeId;
    private String unitId;
    private String result;
    private Integer xpGained;
    private Instant timestamp;
}