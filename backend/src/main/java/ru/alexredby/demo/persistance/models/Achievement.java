package ru.alexredby.demo.persistance.models;

import com.ibasco.agql.protocols.valve.steam.webapi.pojos.SteamGameAchievementSchema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "ACHIEVEMENTS", uniqueConstraints = @UniqueConstraint(columnNames = {"application_id", "name"}))
@NoArgsConstructor
@Data
public class Achievement implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(nullable = false, updatable = false)
    private Long id;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", nullable = false, updatable = false)
    private Application application;

    @Column(name = "name", nullable = false, updatable = false)
    private String name;

    private String displayName;
    private boolean hidden;
    private String description;
    private String icon;
    private String grayIcon;
    private double percentage;

//    public Achievement(int appId, SteamGameAchievementSchema achievementSchema) {
//        var app = new Application();
//        app.setId(appId);
//
//        this(app, achievementSchema);
//    }

    public Achievement(
            Application application,
            SteamGameAchievementSchema achievementSchema,
            double percentage
    ) {
        this.application = application;
        this.name = achievementSchema.getName();
        this.displayName = achievementSchema.getDisplayName();
        this.hidden = achievementSchema.getHidden() == 1;
        this.description = achievementSchema.getDescription();
        this.icon = achievementSchema.getIcon();
        this.grayIcon = achievementSchema.getIcongray();
        this.percentage = percentage;
    }
}
