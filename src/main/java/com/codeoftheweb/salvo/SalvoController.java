package com.codeoftheweb.salvo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static java.util.stream.Collectors.toList;


@RestController
@RequestMapping("/api")
public class SalvoController {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private GamePlayerRepository gamePlayerRepository;

    @Autowired
    private GameRepository gameRepository;

    @RequestMapping("/leaderboard")
    public List<Object> getLeaderboardMap() {
        return playerRepository.findAll().stream().map(player -> createLeaderboardMap(player)).collect(toList());
    }

    @RequestMapping("/game_view/{nn}")
    public ResponseEntity<Map<String, Object>> findGamePlayer(@PathVariable long nn, Authentication authentication) {
        GamePlayer requestedGP = gamePlayerRepository.findById(nn);

        if (requestedGP.getPlayer() == authenticatedUser(authentication)){

            return new ResponseEntity<>(findgamePlayer(nn), HttpStatus.OK);
        }

        else {
            return new ResponseEntity<>(makeMapforError("Error", "You are unauthorized to see this page!"), HttpStatus.UNAUTHORIZED);
        }
    }

    public Map<String, Object> findgamePlayer(Long nn) {
        Map<String, Object> gameMapSet = new LinkedHashMap<>();
        GamePlayer gamePlayerId = gamePlayerRepository.findById(nn).get();
        gameMapSet.put("Info", createGameMap(gamePlayerId.getGame()));
        gameMapSet.put("thisPlayer", createThisPlayerMap(gamePlayerId));
        gameMapSet.put("Ships", createShipMap(gamePlayerId.getShips()));
        gameMapSet.put("Salvoes", createSalvoMaps(gamePlayerId.getGame().gamePlayer)); // salvo
        return gameMapSet;
    }


    @RequestMapping("/games") // see all games and the related information
    // this method is called when someone requests /games
    public Map<String, Object> getGamesandCurrentPlayer() {
        Map<String, Object> newMap = new LinkedHashMap<>();
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();
        if (authentication.getName() == "anonymousUser") {
            newMap.put("currentUser", null);

        } else {
            newMap.put("currentUser", createPlayerMap(authenticatedUser(authentication)));
        }
        newMap.put("games", gameRepository.findAll().stream().map(game -> createGameMap(game)).collect(toList()));
        return newMap;
    }
/*    public List<Object> getGameMap() {
        return gameRepository.findAll().stream().map(game->createGameMap(game)).collect(toList());
    }*/


    private List<Map<String, Object>> createShipMap(Set<Ship> ships) {
        return ships.stream().map(ship -> createShipMaps(ship)).collect(toList());
    }


    private List<Map<String, Object>> createGamePlayerMap(Set<GamePlayer> gamePlayer) {
        return gamePlayer.stream().map(gameplayer -> createGamePlayerMaps(gameplayer)).collect(toList());
    }

    private List<Map<String, Object>> createSalvoMaps(Set<GamePlayer> gamePlayer) { // salvos
        return gamePlayer.stream().map(gameplayer -> createFinalSalvoMap(gameplayer)).collect(toList());
    }

    private List<Map<String, Object>> createTurnMap(Set<Salvo> salvoes) { // salvos
        return salvoes.stream().map(salvo -> createTurnMaps(salvo)).collect(toList());
    }

    public Player authenticatedUser(Authentication authentication) {
        return playerRepository.findByEmail(authentication.getName());
    }

    private Map<String, Object> createShipMaps(Ship ship) {
        Map<String, Object> shipMap = new LinkedHashMap<String, Object>();
        shipMap.put("type", ship.getType());
        shipMap.put("location", ship.getLocation());
        return shipMap;

    }

    private Map<String, Object> createTurnMaps(Salvo salvo) { // salvo
        Map<String, Object> createdTurnMap = new LinkedHashMap<String, Object>();
        createdTurnMap.put("turn", salvo.getTurn());
        createdTurnMap.put("Locations", salvo.getLocation());
        return createdTurnMap;

    }

    private Map<String, Object> createGameMap(Game game) {
        Map<String, Object> gamemap = new LinkedHashMap<String, Object>();
        gamemap.put("GameId", game.getId()); // i can get the Game id
        gamemap.put("creationDate", game.getDate()); // i can get the Date
        gamemap.put("gamePlayers", createGamePlayerMap(game.gamePlayer)); // i can get the gamePlayers
        return gamemap;
    }

    private Map<String, Object> createGamePlayerMaps(GamePlayer gamePlayer) {
        Map<String, Object> createdGamePlayerMap = new LinkedHashMap<String, Object>();
        createdGamePlayerMap.put("gamePlayerId", gamePlayer.getId()); // i get the GamePlayerId i should have two of these
        createdGamePlayerMap.put("scoreInThisGame", gamePlayer.getGame().getScore(gamePlayer.getPlayer()));
        createdGamePlayerMap.put("player", createPlayerMap(gamePlayer.getPlayer()));
        return createdGamePlayerMap;
    }


    private Map<String, Object> createFinalSalvoMap(GamePlayer gamePlayer) { // salvo
        Map<String, Object> createdSalvoMap = new LinkedHashMap<String, Object>();
        createdSalvoMap.put(gamePlayer.getGamePlayerIdString(), createTurnMap(gamePlayer.getSalvo()));
        return createdSalvoMap;
    }

    private Map<String, Object> createPlayerMap(Player player) {
        Map<String, Object> createdPlayerMap = new LinkedHashMap<String, Object>();
        createdPlayerMap.put("playerId", player.getId()); // i get the PlayerId should have two of these
        createdPlayerMap.put("playerEmail", player.getEmail());
        return createdPlayerMap;
    }

    private Map<String, Object> createThisPlayerMap(GamePlayer gamePlayer) {
        Map<String, Object> createdThisPlayerMap = new LinkedHashMap<String, Object>();
        createdThisPlayerMap.put("gamePlayerId", gamePlayer.getId()); //
        createdThisPlayerMap.put("playerEmail", gamePlayer.getPlayer().getEmail());
        createdThisPlayerMap.put("playerId", gamePlayer.getPlayer().getId());
        return createdThisPlayerMap;
    }

    private Map<String, Object> createLeaderboardMap(Player player) {
        Map<String, Object> createdLeaderboardMap = new LinkedHashMap<String, Object>();
        createdLeaderboardMap.put("email", player.getEmail()); //
        createdLeaderboardMap.put("totalScore", pass(player.score));
        return createdLeaderboardMap;
    }

    private List<Object> pass(Set<Score> scores) {
        return scores.stream().map(score -> passFurther(score)).collect(toList());
    }

    private List<Object> passFurther(Score score) {
        List<Object> createdScoreMap = new ArrayList<>();
        createdScoreMap.add(score.getPlayerScore()); //
        return createdScoreMap;
    }

    @RequestMapping(value = "/players", method = RequestMethod.POST)
    public ResponseEntity signUp(ModelMap model,
                                                @RequestParam String email,
                                                @RequestParam String password) {

        if (playerRepository.findByEmail(email) == null) {
            playerRepository.save(new Player(email, password));
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Name is in use" ,HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping (value = "/getGamePlayerID", method = RequestMethod.POST)

    private Long returnCorrectGamePlayerId(@RequestParam long gameId,
                                     @RequestParam long playerId) {
     return customFunc(gameRepository.findById(gameId), playerId);


    }

    private Long customFunc(Game game, Long playerId){

        return game.customFunction(game.gamePlayer, playerId);

    }

    private Map<String, Object> makeMapforError(String status, String value) {
        Map<String, Object> errorMap = new HashMap<>();
        errorMap.put("status", status);
        errorMap.put("message", value);
        return errorMap;
    }



}