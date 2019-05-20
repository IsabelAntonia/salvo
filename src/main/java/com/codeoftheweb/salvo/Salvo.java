package com.codeoftheweb.salvo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

// a salvo are all the shots in one turn


@Entity
public class Salvo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private int turn;

    @ElementCollection
    @Column(name = "location")
    private List<String> location = new ArrayList<>();;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="gamePlayer_id")
    private GamePlayer gamePlayer;

    public Salvo(){}

    public Salvo(int turn, List<String> location) {
        this.turn = turn;
        this.location = location;
        this.gamePlayer = getGamePlayer();
    }

    public int nextTurn() {
        return turn++;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public List<String> getLocation() {
        return location;
    }

    public void setLocation(List<String> location) {
        this.location = location;
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }



    public int getHits(List<String> location, List<String> shipsLocs) {

        List<String> hits = new ArrayList<>();
        location.forEach(shot -> {
            if (shipsLocs.contains(shot)) {
                hits.add(shot);
            }
        });
        return hits.size();
    }



    public List<String> shipsList(Set<Ship> ships) {
        List<String> shipsList = new ArrayList<>();
        for (Ship ship: ships) {
            for (String location: ship.getLocation()) {
                shipsList.add(location);
            }
        }
        return shipsList;
    }

}
