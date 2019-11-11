package ru.alexredby.demo.models;

import com.ibasco.agql.protocols.valve.steam.webapi.pojos.SteamApp;
import lombok.Data;

import javax.persistence.*;

// TODO: more data such achievements
@Entity
@Table(name = "APPLICATIONS")
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
