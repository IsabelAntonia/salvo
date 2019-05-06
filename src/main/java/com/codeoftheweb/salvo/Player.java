package com.codeoftheweb.salvo;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Player {

    private String userName;
    private String email;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToMany(mappedBy="player", fetch= FetchType.EAGER)
    Set<GamePlayer> gamePlayer;

    @OneToMany(mappedBy = "player", fetch= FetchType.EAGER)
    Set <Score> score;

    public Player() {}

    public Player (String userName, String email) {
        this.userName = userName;
        this.email = email;
    }

    public Score getSingleScore(Game game) { // i know this player
        for (Score score: score) { // check all the scores he got
            if (score.getGame() == game) { // if the score is from this game
                return score; // return it
            }
        }
        return null;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String toString() {
        return userName;
    }

    public Set<Score> getScore() {
        return score;
    }

    public void setScore(Set<Score> score) {
        this.score = score;
    }
}