package com.liwanag.gamification.dto.event;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "eventType"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = AnswerEvaluatedEvent.class, name = "ANSWER_EVALUATED"),
        @JsonSubTypes.Type(value = ActivityCompletedEvent.class, name = "ACTIVITY_COMPLETED"),
        @JsonSubTypes.Type(value = EpisodeCompletedEvent.class, name = "EPISODE_COMPLETED"),
        @JsonSubTypes.Type(value = UnitCompletedEvent.class, name = "UNIT_COMPLETED")
})
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
