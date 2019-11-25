package ru.alexredby.demo.persistance.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Duration;
import java.util.List;

@Entity
@Table(name = "USER_APPLICATIONS", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "application_id"}))
@NoArgsConstructor
@Data
public class UserApplication {
    @Id
    @Column(nullable = false, updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", nullable = false, updatable = false)
    private Application application;

    @Column(nullable = false, updatable = false)
    private Duration totalPlaytime;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userApplication")
    private List<UserAchievement> achievements;
}
