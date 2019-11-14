package ru.alexredby.demo.persistance.models;

import com.ibasco.agql.protocols.valve.steam.webapi.pojos.SteamApp;
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

    public Application(SteamApp app) {
        this.id = app.getAppid();
        this.name = app.getName();
    }
}
