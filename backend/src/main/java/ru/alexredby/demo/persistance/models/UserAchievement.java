package ru.alexredby.demo.persistance.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.alexredby.demo.persistance.models.Ids.UserAchievementId;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "USER_ACHIEVEMENTS")
@IdClass(UserAchievementId.class)
@NoArgsConstructor
@Data
public class UserAchievement implements Serializable {
    // TODO: Mb change to eager
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_application_id", nullable = false, updatable = false)
    private UserApplication userApplication;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "achievement_id", nullable = false, updatable = false)
    private Achievement achievement;

    @Column(nullable = false, updatable = false)
    private LocalDateTime unlockTime;
}
