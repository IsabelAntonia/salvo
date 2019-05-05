package com.codeoftheweb.salvo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;
import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


@Entity
public class GamePlayer {

    @CreationTimestamp
    private Date date;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="player_id")
    private Player player;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="game_id")
    private Game game;

    @OneToMany(mappedBy = "gamePlayer" ,fetch = FetchType.EAGER)
    Set<Ship> ships = new HashSet<>();

    @OneToMany(mappedBy = "gamePlayer" ,fetch = FetchType.EAGER)
    @OrderBy("turn asc")
    Set<Salvo> salvoes = new HashSet<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    public GamePlayer() {}

    public GamePlayer(Game game, Player player) {
        this.game = game;
        this.player = player;

    }

    public void addShip(Ship ship) {
        ship.setGamePlayer(this);
        this.ships.add(ship);
    }

    public void addSalvo(Salvo salvo) {
        salvo.setGamePlayer(this);
        this.salvoes.add(salvo);

    }

    public Set<Salvo> getSalvo() {
        return salvoes;
    }

    public void setSalvo(Set<Salvo> salvo) {
        this.salvoes = salvo;
    }

    public Set<Ship> getShips() {
        return ships;
    }

    public void setShips(Set<Ship> ship) {
        this.ships = ship;
    }

    public Player getPlayers() {
        return this.player;
    }

    public Game getGames() {
        return this.game;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getGamePlayerIdString() {
        return Long.toString(getId());}

}