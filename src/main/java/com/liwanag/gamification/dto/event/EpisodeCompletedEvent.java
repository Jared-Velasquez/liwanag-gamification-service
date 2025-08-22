package com.liwanag.gamification.dto.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class EpisodeCompletedEvent extends LiwanagEvent {
    private UUID userId;
    private String episodeId;
    private String unitId;
    private Instant timestamp;
}
