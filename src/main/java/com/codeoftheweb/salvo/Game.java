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
    Set<GamePlayer> gamePlayer;

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
}
