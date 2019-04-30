package com.codeoftheweb.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;


@RestController
@RequestMapping("/api")
public class SalvoController {


    @Autowired
    private GamePlayerRepository gamePlayerRepository;

    @RequestMapping("/games")
    public List <Object> getGamePlayers(){
        return gamePlayerRepository.findAll().stream().map(gamePlayer -> getGamePlayers(gamePlayer)).collect(toList());
    }


    private Map<String, Object> getGamePlayers(GamePlayer gamePlayer) {
        Map<String, Object> gamemap = new LinkedHashMap<String, Object>();
        Map<String, Object> gamePlayerMap = new LinkedHashMap<String, Object>();
        Map<String, Object> playerMap = new LinkedHashMap<String, Object>();

        playerMap.put("playerId", gamePlayer.getPlayer().getId());
        playerMap.put("email", gamePlayer.getPlayer().getEmail());
        gamePlayerMap.put("GamePlayerId", gamePlayer.getId());
        gamePlayerMap.put("player", playerMap);
        gamemap.put("GameId", gamePlayer.getGame().getId());
        gamemap.put("creationDate", gamePlayer.getDate());
        gamemap.put("gamePlayers", gamePlayerMap);



        return gamemap;
    }
}
