package com.liwanag.gamification.dto.event;

public abstract class LiwanagEvent {
    public enum EventType {
        ANSWER_EVALUATED,
        ACTIVITY_COMPLETED,
        EPISODE_COMPLETED,
        UNIT_COMPLETED
    }

    private String eventType;

    public EventType getEnumEventType() throws IllegalArgumentException {
        return EventType.valueOf(this.eventType);
    }
}
