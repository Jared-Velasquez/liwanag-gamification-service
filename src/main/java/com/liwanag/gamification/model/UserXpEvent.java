package com.liwanag.gamification.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
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
    private Timestamp timestamp;
}
