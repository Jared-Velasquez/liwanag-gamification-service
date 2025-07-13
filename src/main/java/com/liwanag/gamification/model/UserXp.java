package com.liwanag.gamification.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "t_user_xp")
@RedisHash("user")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserXp {
    @Id
    private UUID userId;
    private Integer xp;
    private Instant lastUpdated;
}
