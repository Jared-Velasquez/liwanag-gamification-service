package com.liwanag.gamification.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "t_user_experience")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED) // App code should not use NoArgsConstructor, only Hibernate can use it
public class UserExperience {
    @Id
    @NonNull
    private UUID userId;
    @NonNull
    private Integer experience;

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private Instant createdAt;
    @LastModifiedDate
    @Column(nullable = false)
    private Instant updatedAt;
}
