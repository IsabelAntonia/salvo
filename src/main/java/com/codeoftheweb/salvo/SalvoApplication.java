package com.codeoftheweb.salvo;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class SalvoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}



	@Bean //  tells Spring 'here is an instance of this class, please keep hold of it and give it back to me when I ask'.
	// Spring knows that the initData() method in the CommandLineRunner will need an instance of PlayerRepo.
	// Spring will inject an instance of PlayerRepository for this parameter
	public CommandLineRunner initData(PlayerRepository playerRepository, GameRepository gameRepository, GamePlayerRepository gamePlayerRepository, ShipRepository shipRepository, SalvoRepository salvoRepository) {
		return (args) -> {

			Player player1 = new Player("Jack", "jack@gmail.com");
			Player player2 = new Player("Chloe", "chloe@gmail.com");
			Player player3 = new Player("Kim", "kim@gmail.com");
			Player player4 = new Player("David", "david@gmail.com");
			Player player5 = new Player("Michelle", "michaelle@gmail.com");

			playerRepository.save(player1); // save the instance in the database
			playerRepository.save(player2);
			playerRepository.save(player3);
			playerRepository.save(player4);
			playerRepository.save(player5);


			Game game1 = new Game ();
			Game game2 = new Game ();
			Game game3 = new Game ();

			gameRepository.save(game1);
			gameRepository.save(game2);
			gameRepository.save(game3);


			GamePlayer gamePlayer1 = new GamePlayer(game3, player5); //Michelle
			GamePlayer gamePlayer2 = new GamePlayer(game3, player2);
			GamePlayer gamePlayer3 = new GamePlayer(game2, player1);
			GamePlayer gamePlayer4 = new GamePlayer(game1, player5);

			List<String> loc1 = Arrays.asList("E3","E4","E5","E7","E8");
			List<String> loc2 = Arrays.asList("A5","B5","C5","D5");
			List<String> loc3 = Arrays.asList("H1","H2","H3");
			List<String> loc4 = Arrays.asList("I5","I6","I7");
			List<String> loc5 = Arrays.asList("F5","G5");

			List<String> salvoLoc1 = Arrays.asList("C3","G5");
			List<String> salvoLoc2 = Arrays.asList("A4", "H10");
			List<String> salvoLoc3 = Arrays.asList("J5", "C8", "A5");


			Ship ship1 = new Ship("carrier", loc1);
			Ship ship2 = new Ship("battleship", loc2);
			Ship ship3 = new Ship("destroyer", loc3);
			Ship ship4 = new Ship("Submarine", loc4);
			Ship ship5 = new Ship("portalBoat", loc5);

			Salvo salvo1 = new Salvo(1, salvoLoc1);
			Salvo salvo2 = new Salvo(2, salvoLoc2);
			Salvo salvo3 = new Salvo(1, salvoLoc3);


			gamePlayerRepository.save(gamePlayer1);
			gamePlayerRepository.save(gamePlayer2);
			gamePlayerRepository.save(gamePlayer3);
			gamePlayerRepository.save(gamePlayer4);

            gamePlayer1.addShip(ship1); // Michelle placed three ships carrier, battleship and destroyer
            gamePlayer1.addShip(ship2);
            gamePlayer1.addShip(ship3);

            gamePlayer1.addSalvo(salvo1);
            gamePlayer1.addSalvo(salvo2);
            gamePlayer2.addSalvo(salvo3);


			shipRepository.save(ship1);
			shipRepository.save(ship2);
			shipRepository.save(ship3);
			shipRepository.save(ship4);
			shipRepository.save(ship5);

			salvoRepository.save(salvo1);
			salvoRepository.save(salvo2);
			salvoRepository.save(salvo3);
		};
	}

}