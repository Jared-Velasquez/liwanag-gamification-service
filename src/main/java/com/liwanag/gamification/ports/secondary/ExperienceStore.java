package com.liwanag.gamification.ports.secondary;

import com.liwanag.gamification.domain.experience.Experience;

import java.util.Optional;
import java.util.UUID;

public interface ExperienceStore {
    Optional<Experience> load(UUID userId);
    void save(Experience experience);
}
