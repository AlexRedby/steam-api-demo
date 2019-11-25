package ru.alexredby.demo.persistance.models;

import com.ibasco.agql.protocols.valve.steam.webapi.pojos.SteamGameAchievementSchema;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "ACHIEVEMENTS", uniqueConstraints = @UniqueConstraint(columnNames = {"application_id", "name"}))
@NoArgsConstructor
@Data
public class Achievement {
    @Id
    @Column(nullable = false, updatable = false)
    private Long id;

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

//    public Achievement(int appId, SteamGameAchievementSchema achievementSchema) {
//        var app = new Application();
//        app.setId(appId);
//
//        this(app, achievementSchema);
//    }

    public Achievement(Application application, SteamGameAchievementSchema achievementSchema) {
        this.application = application;
        this.name = achievementSchema.getName();
        this.displayName = achievementSchema.getDisplayName();
        this.hidden = achievementSchema.getHidden() == 1;
        this.description = achievementSchema.getDescription();
        this.icon = achievementSchema.getIcon();
        this.grayIcon = achievementSchema.getIcongray();
    }
}
