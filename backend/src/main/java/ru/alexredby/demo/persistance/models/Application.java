package ru.alexredby.demo.persistance.models;

import com.ibasco.agql.protocols.valve.steam.webapi.pojos.SteamPlayerOwnedGame;
import com.ibasco.agql.protocols.valve.steam.webapi.pojos.StoreAppDetails;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

// TODO: more data such achievements
@Entity
@Table(name = "APPLICATIONS")
@NoArgsConstructor
@Data
public class Application {
    @Id
    @Column(nullable = false, updatable = false)
    private int id;
    @Column(nullable = false)
    private String name;

    public Application(StoreAppDetails app) {
        this.id = app.getAppId();
        this.name = app.getName();
    }

    /**
     * Warning: Not recommended to use,
     *          only if steam store didn't return game
     *
     * @param app
     */
    public Application(SteamPlayerOwnedGame app) {
        System.out.println(app);
        this.id = app.getAppId();
        this.name = app.getName();
    }
}
