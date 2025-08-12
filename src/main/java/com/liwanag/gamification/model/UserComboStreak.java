package com.liwanag.gamification.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "t_user_combo_streak")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserComboStreak {
    @Id
    private UUID userId;
    private Integer streak;
    private Integer maxStreak;
    private LocalDate lastActiveDate;
}
