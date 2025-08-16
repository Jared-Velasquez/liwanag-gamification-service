package com.liwanag.gamification.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "t_user_experience")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserExperience {
    @Id
    private UUID userId;
    private Integer experience;
    private Instant lastUpdated;
}
