package com.liwanag.gamification.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "t_user_xp_events")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserXpEvent {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.AUTO)
    private UUID id;
    private UUID userId;
    private Integer deltaXp;
    private UUID eventId;
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;
}
