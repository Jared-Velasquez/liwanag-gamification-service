package com.liwanag.gamification.dto;

import java.time.Instant;

public record AnswerEvaluatedEvent(String userId,
                                   String questionId,
                                   String activityId,
                                   String episodeId,
                                   String unitId,
                                   String result,
                                   Integer xpGained,
                                   Instant timestamp) {
}
