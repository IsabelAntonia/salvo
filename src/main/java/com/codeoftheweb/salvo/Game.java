package com.codeoftheweb.salvo;
import javax.persistence.*;
import javax.persistence.GenerationType;
import java.util.Date;
import java.util.Set;
import org.hibernate.annotations.CreationTimestamp;

@Entity
public class Game {

    @CreationTimestamp
    private Date date;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToMany(mappedBy="game", fetch= FetchType.EAGER)
    @OrderBy("id asc")
    Set<GamePlayer> gamePlayer;

    @OneToMany(mappedBy="game", fetch= FetchType.EAGER)
    Set<Score> score;

    public Game() { }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Set<Score> getScore() {
        return score;
    }

    public void setScore(Set<Score> score) {
        this.score = score;
    }

    public Score getScore(Player player){
        return player.getSingleScore(this);
    }

    public Long customFunction (Set <GamePlayer> gamePlayers, long playerId){

        for (GamePlayer gamePlayer: gamePlayers){

            if (gamePlayer.getPlayer().getId() == playerId){
                return gamePlayer.getId();
            }


        }
        return null;
    }


    public long getNumberOfPlayers(){
        if (this.gamePlayer.size() == 2 ){
            return 2;
        }
        return 1;
    }

    public GamePlayer getOpponent(GamePlayer thisGamePlayer) {
        for (GamePlayer opponent: this.gamePlayer) {
            if (thisGamePlayer.getId() != opponent.getId()) {
                return opponent;
            }
        }
        return null;
    }





}
