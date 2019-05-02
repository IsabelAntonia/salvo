package com.codeoftheweb.salvo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
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



    // some player want to see this info e.g player 50 for a specific game he is playing
    // we want to return the correct data for him so that JS on the frontend can easily present it
    // see information for one specific game from one specific players point of view by gameplayer
    // more specifically see the name of this player and his id , the name of his opponent and his id, what kind of ships he (this player) has placed, also where those ships are




    @RequestMapping("/game_view/{nn}")
    public Map<String, Object> getGameInfo(@PathVariable long nn) {
        return gamePlayerMapInfo(gamePlayerRepository.findById(nn).get());
    }

/*    @RequestMapping("/game_view/{nn}")
    public Map<String, Object> findgamePlayer(@PathVariable Long nn) {
        Map<String, Object> gameMapInfo = new LinkedHashMap<>();
        GamePlayer gamePlayerId = gamePlayerRepository.findById(nn).get();
        gameMapInfo.put("GamePlayerId", nn);
        gameMapInfo.put("ThisPlayerId", gamePlayerId.getPlayer().getId());

        return gameMapInfo;
    }*/



    @RequestMapping("/games") // see all games and the related information
    // this method is called when someone requests /games
    public List <Object> getGamePlayers(){
        return gamePlayerRepository.findAll().stream().map(gamePlayer -> getGamePlayer(gamePlayer)).collect(toList());
    }


    private Map<String, Object> getGamePlayer(GamePlayer gamePlayer) {
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

    private Map<String, Object> gamePlayerMapInfo (GamePlayer gamePlayer){
        Map<String, Object> gameMapInfo = new LinkedHashMap<>();
        gameMapInfo.put("ThisPlayerId", gamePlayer.getPlayer().getId());
        return gameMapInfo;
    }



}
