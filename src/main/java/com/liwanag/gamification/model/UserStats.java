package com.liwanag.gamification.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "t_user_question_stats")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserStats {
    @Id
    @NonNull
    private UUID userId;
    @NonNull
    private Integer attemptedCount;
    @NonNull
    private Integer correctCount;
    @NonNull
    private Integer activityCompletedCount;
    @NonNull
    private Integer episodeCompletedCount;
    @NonNull
    private Integer maxUnitCompleted;

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private Instant createdAt;
    @LastModifiedDate
    @Column(nullable = false)
    private Instant updatedAt;
}