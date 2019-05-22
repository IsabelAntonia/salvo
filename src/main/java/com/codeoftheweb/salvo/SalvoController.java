package com.codeoftheweb.salvo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

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

    @Autowired
    private ShipRepository shipRepository;

    @Autowired
    private SalvoRepository salvoRepository;

    @Autowired
    private ScoreRepository scoreRepository;

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
            return new ResponseEntity<>(makeMapforStatus("Error", "You are unauthorized to see this page!"), HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping("/game/{mm}/players")
    public ResponseEntity<Map<String, Object>> joinGame(@PathVariable long mm, Authentication authentication) {
        if (authentication != null) {

            if (gameRepository.findById(mm) ==  null) {// check if game exists
                return new ResponseEntity<>(makeMapforStatus("Error","No such game"),HttpStatus.BAD_REQUEST);

            }

            else {

                Game game = gameRepository.findById(mm);
                if (game.getNumberOfPlayers() == 2){ // check if game is full
                    return new ResponseEntity<>(makeMapforStatus("Error","Game is full"),HttpStatus.BAD_REQUEST);
                }

                else {

                    GamePlayer presentGamePlayer = game.gamePlayer.iterator().next();

                    if (presentGamePlayer.getPlayer() == authenticatedUser(authentication)) { // check if i am the one gameplayer
                        return new ResponseEntity<>(makeMapforStatus("Error","You are already a player in this game"),HttpStatus.BAD_REQUEST);
                    }

                    else {
                        GamePlayer gamePlayer = new GamePlayer(game, authenticatedUser(authentication));
                        gamePlayerRepository.save(gamePlayer);
                        return new ResponseEntity<>(createMapForGameCreation("gpid", gamePlayer.getId()), HttpStatus.CREATED);
                    }
                }
            }


        } else {
            return new ResponseEntity<>(makeMapforStatus("Error","Login to join a game!"),HttpStatus.UNAUTHORIZED);
        }
    }



    public Map<String, Object> findgamePlayer(Long nn) {
        Map<String, Object> gameMapSet = new LinkedHashMap<>();
        GamePlayer gamePlayerId = gamePlayerRepository.findById(nn).get();
        gameMapSet.put("Info", createGameMap(gamePlayerId.getGame()));
        gameMapSet.put("Winner", findFinalWinner(gamePlayerId));
        gameMapSet.put("thisPlayer", createThisPlayerMap(gamePlayerId));
        gameMapSet.put("gameHistory", createGameHistory(gamePlayerId.getGame().gamePlayer)); // getting two gameplayers
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


    private List<Map<String, Object>> createShipMap(Set<Ship> ships) {
        return ships.stream().map(ship -> createShipMaps(ship)).collect(toList());
    }

    private List<Map<String, Object>> createGameHistory(Set<GamePlayer> gamePlayer) {
        return gamePlayer.stream().map(gameplayer -> createHistoryMapForEachGamePlayer(gameplayer)).collect(toList());
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

    private List<Map<String, Object>> createHisMap(Set<Salvo> salvoes) { // salvos
        return salvoes.stream().map(salvo -> createHisMaps(salvo)).collect(toList());
    }

    private int findTotalHits(Set<Salvo> salvoes) { // salvos
        List<Integer> mySumList = new ArrayList();
        mySumList =  salvoes.stream().map(salvo -> salvo.getHits(salvo.getLocation(), salvo.shipsList(salvo.getGamePlayer().getGame().getOpponent(salvo.getGamePlayer()).getShips()))).collect(Collectors.toList());
        int sum = mySumList.stream().reduce(0, Integer::sum);
    return sum;
    }

    private int findLastTurn(Set<Salvo> salvoes) { // salvos

        if (salvoes.size() > 0){
        List<Integer> myTurnList = new ArrayList();
        myTurnList =  salvoes.stream().map(salvo -> salvo.getTurn()).collect(Collectors.toList());
        int lastTurn = Collections.max(myTurnList);
        return lastTurn;
        }

        return 0;
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


    private Map<String, Object> createHistoryMapForEachGamePlayer(GamePlayer gamePlayer) {
        Map<String, Object> historyMap = new LinkedHashMap<String, Object>();
        historyMap.put(gamePlayer.getGamePlayerIdString(), createHisMap(gamePlayer.getSalvo()));
        return historyMap;

    }

    private Map<String, Object> createHisMaps(Salvo salvo) {
        Map<String, Object> createdHisMap = new LinkedHashMap<String, Object>();
        createdHisMap.put("turnNumber", salvo.getTurn());
        createdHisMap.put("allHitsInTurn", salvo.getHits(salvo.getLocation(), salvo.shipsList(salvo.getGamePlayer().getGame().getOpponent(salvo.getGamePlayer()).getShips())));
        // sending the list of ships of opponent and the list of locations from salvo
        return createdHisMap;

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
        gamemap.put("gameOver", gameIsOver(game));
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
        createdLeaderboardMap.put("allScores", pass(player.score));
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

    @RequestMapping(value = "/games", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> createGame(
            Authentication authentication) {

        if (authentication != null) {
            Game game = new Game();
            gameRepository.save(game);
            GamePlayer gamePlayer = new GamePlayer(game, authenticatedUser(authentication));
            gamePlayerRepository.save(gamePlayer);
            return new ResponseEntity<>(createMapForGameCreation("gpid", gamePlayer.getId()), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(makeMapforStatus("Error","Login to create a game!"),HttpStatus.UNAUTHORIZED);
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

    private Map<String, Object> makeMapforStatus(String status, String value) {
        Map<String, Object> errorMap = new HashMap<>();
        errorMap.put("status", status);
        errorMap.put("message", value);
        return errorMap;
    }

    private Map<String, Object> createMapForGameCreation(String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return map;
    }

    @RequestMapping (value ="/games/players/{gpid}/ships", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> addPlacedShips(@PathVariable long gpid, @RequestBody Set<Ship> newShip, Authentication authentication) {
        GamePlayer gP = gamePlayerRepository.findById(gpid);
        if (authentication != null) {

            if (gP.getPlayer() != authenticatedUser(authentication)){ // authenticated user has to be gpid
                return new ResponseEntity<>(makeMapforStatus("Error", "You are unauthorized to see this page!"), HttpStatus.UNAUTHORIZED);
            }
            else {

                if(gP.ships.size() > 5 || newShip.size() > 5){ // check if user has not already placed ships
                    return new ResponseEntity<>(makeMapforStatus("Error", "You can not place more than 5 ships!"), HttpStatus.FORBIDDEN);
                }

                else {
                    for (Ship ship : newShip){
                        gP.addShip(ship);
                        shipRepository.save(ship);
                    }
                    return new ResponseEntity<>(makeMapforStatus("Success", "New Ship added!"),HttpStatus.CREATED);
                }

            }
        }

        else {
            return new ResponseEntity<>(makeMapforStatus("Error","Login to place ships!"),HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping (value ="/games/players/{gpid}/salvoes", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> addSalvoes(@PathVariable long gpid, @RequestBody Salvo salvo, Authentication authentication) {
        GamePlayer gP = gamePlayerRepository.findById(gpid);
        if (authentication != null) {

            if (gP.getPlayer() != authenticatedUser(authentication)){ // authenticated user has to be gpid
                return new ResponseEntity<>(makeMapforStatus("Error", "You are unauthorized to see this page!"), HttpStatus.UNAUTHORIZED);
            }
            else {

                List <Integer> allTurns = gP.salvoes.stream().map(singleSalvo -> singleSalvo.getTurn()).collect(toList());
           if (allTurns.contains(salvo.getTurn())){
               return new ResponseEntity<>(makeMapforStatus("Error", "You fired already in this turn!"), HttpStatus.FORBIDDEN);
           } // the user has already submitted a salvo for the turn listed

           else if (gameOver(gP)){
               return new ResponseEntity<>(makeMapforStatus("Error", "Game is over!"), HttpStatus.FORBIDDEN);
           }
           else {
               gP.addSalvo(salvo);
               salvoRepository.save(salvo);
               return new ResponseEntity<>(makeMapforStatus("Success", "Salvo was fired!"),HttpStatus.CREATED);

           }

            }
        }

        else {
            return new ResponseEntity<>(makeMapforStatus("Error","Login to place ships!"),HttpStatus.UNAUTHORIZED);
        }
    }

    private String findFinalWinner(GamePlayer gameplayer){

        String tied = "tie";
        String noWinner = "none";


        if (findTotalHits(gameplayer.getGame().getOpponent(gameplayer).getSalvo()) != 17 && findTotalHits(gameplayer.salvoes) == 17 && (findLastTurn(gameplayer.salvoes) == findLastTurn(gameplayer.getGame().getOpponent(gameplayer).getSalvo()))){
            Score score = new Score(1, gameplayer.getGame(), gameplayer.getPlayer()); // this Player on
            scoreRepository.save(score);
            return gameplayer.getPlayer().getEmail();
        }

        else if (findTotalHits(gameplayer.salvoes) != 17 && findTotalHits(gameplayer.getGame().getOpponent(gameplayer).getSalvo()) == 17 && (findLastTurn(gameplayer.salvoes) == findLastTurn(gameplayer.getGame().getOpponent(gameplayer).getSalvo()))){
            Score score = new Score(1, gameplayer.getGame(), gameplayer.getGame().getOpponent(gameplayer).getPlayer()); // this Player on
            scoreRepository.save(score);
            return gameplayer.getGame().getOpponent(gameplayer).getPlayer().getEmail();
        }

        else if (findTotalHits(gameplayer.salvoes) == 17 && findTotalHits(gameplayer.getGame().getOpponent(gameplayer).getSalvo()) == 17 && findLastTurn(gameplayer.salvoes) == findLastTurn(gameplayer.getGame().getOpponent(gameplayer).getSalvo())){

            Score scoreOp = new Score(0.5, gameplayer.getGame(), gameplayer.getGame().getOpponent(gameplayer).getPlayer()); // this Player on
            Score score = new Score(0.5, gameplayer.getGame(), gameplayer.getPlayer()); // this Player on
            scoreRepository.save(score);
            scoreRepository.save(scoreOp);
            return tied ;
        }

        return noWinner;
    }

    private boolean gameOver (GamePlayer gameplayer){

        if (gameplayer.getGame().getScore().size() == 2){
            return true;
        }
        return false;
    }

    private boolean gameIsOver (Game game){

        if (game.getScore().size() == 2){
            return true;
        }
        return false;
    }




}