package com.liwanag.gamification.ports.secondary;

import com.liwanag.gamification.domain.statistics.Statistics;

import java.util.Optional;
import java.util.UUID;

public interface StatisticsStore {
    Optional<Statistics> load(UUID userId);
    void save(Statistics statistics);
}
