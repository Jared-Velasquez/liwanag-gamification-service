package com.liwanag.gamification.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.Date;
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
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdated;
}
