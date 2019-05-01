package com.codeoftheweb.salvo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Ship {




    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String type;

    @ElementCollection
    @Column(name = "location")
    private List<String> location = new ArrayList<>();;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="gamePlayer_id")
    private GamePlayer gamePlayer;

public Ship(){}


public Ship(String type, List<String> location){

    this.location = location;
    this.type = type;

}

    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    public long getId() { // get id
        return id;
    }

    public void setId(long id) { // set id
        this.id = id;
    }

    public void setType(String type) { // set type
        this.type = type;
    }

    public String getType() { // get type
        return type;
    }

    public List<String> getLocation() {
        return location;
    }

    public void setLocation(List<String> location) {
        this.location = location;
    }
}
