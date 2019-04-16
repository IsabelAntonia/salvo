package com.codeoftheweb.salvo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity // tells Spring to create a player table for this class
public class Player {

    private String userName;
    private String email;

    @Id // unique value for every instance of player
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    public Player() {}

    public Player (String userName, String email) {
        this.userName = userName;
        this.email = email;
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
}