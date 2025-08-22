package com.liwanag.gamification.dto.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

// Can't use record because we need to manually convert detail-type to detailType
@Getter
@Setter
@RequiredArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Event {
    private String version;
    private String id;

    @JsonProperty("detail-type")
    private String detailType;
    private String source;
    private String account;
    private String time;
    private String region;
    private List<String> resources;
    private LiwanagEvent detail;
}