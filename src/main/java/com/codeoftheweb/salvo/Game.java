package com.codeoftheweb.salvo;
import javax.persistence.*;
import javax.persistence.GenerationType;
import java.util.Date;
import java.util.List;

@Entity
public class Game {

    private Date date;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    @OneToMany(mappedBy="player", fetch= FetchType.EAGER)
    List<GamePlayer> gamePlayer;

    public Game() {
        this.date = new Date();
    }

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
}
