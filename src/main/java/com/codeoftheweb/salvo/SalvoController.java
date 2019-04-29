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
    private GameRepository gameRepository;


    @RequestMapping("/games")
    public List<Object> getGames() {
        return gameRepository.findAll().stream().map(game->gameMap(game)).collect(toList());
    }

    private Map<String, Object> gameMap(Game game) {
        Map<String, Object> gamemap = new LinkedHashMap<String, Object>();
        gamemap.put("id", game.getId());
        gamemap.put("creationDate", game.getDate());

        return gamemap;
    }
}
