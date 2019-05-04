package com.codeoftheweb.salvo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

// a salvo are all the shots in one turn


@Entity
public class Salvo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long turn;

    @ElementCollection
    @Column(name = "location")
    private List<String> location = new ArrayList<>();;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="gamePlayer_id")
    private GamePlayer gamePlayer;

    public Salvo(){}


    public Salvo(List<String> location, long turn){

        this.location = location;
        this.turn = turn;
    }



}
