package com.liwanag.gamification.dto.event;

public class LiwanagEvent {
    public enum EventType {
        AnswerEvaluated,
        ActivityCompleted,
        EpisodeCompleted,
        UnitCompleted
    }

    public static EventType getEnumEventType(String eventType) throws IllegalArgumentException {
        return EventType.valueOf(eventType);
    }
}
