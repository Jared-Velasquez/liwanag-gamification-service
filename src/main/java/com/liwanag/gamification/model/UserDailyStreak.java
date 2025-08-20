package com.liwanag.gamification.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "t_user_daily_streak")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDailyStreak {
    @Id
    private UUID userId;
    private Integer streak;
    private Integer maxStreak;
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastActiveDate;
}
