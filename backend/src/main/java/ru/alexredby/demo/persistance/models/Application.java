package ru.alexredby.demo.persistance.models;

import com.ibasco.agql.protocols.valve.steam.webapi.pojos.SteamPlayerOwnedGame;
import com.ibasco.agql.protocols.valve.steam.webapi.pojos.StoreAppDetails;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

// TODO: more data such achievements
@Entity
@Table(name = "APPLICATIONS")
@NoArgsConstructor
@Data
public class Application implements Serializable {
    @Id
    @Column(nullable = false, updatable = false)
    private int id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private boolean hasAchievements;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "application")
    private List<Achievement> achievements;

    public Application(StoreAppDetails app) {
        this.id = app.getAppId();
        this.name = app.getName();
        this.hasAchievements = app.getAchievements().getTotal() > 0;
        this.achievements = new ArrayList<>();
    }

    /**
     * Warning: Not recommended to use, only if steam store didn't return game(removed from it)
     *
     * @param app
     */
    public Application(SteamPlayerOwnedGame app) {
        this.id = app.getAppId();
        this.name = app.getName();
        // TODO: Sometimes it can have achievement
        this.hasAchievements = false;
        this.achievements = new ArrayList<>();
    }

    public void setAchievements(List<Achievement> achievements) {
        achievements.forEach(a -> a.setApplication(this));
        this.achievements = achievements;
    }
}
