package com.codeoftheweb.salvo;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SalvoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}



	@Bean //  tells Spring 'here is an instance of this class, please keep hold of it and give it back to me when I ask'.
	// Spring knows that the initData() method in the CommandLineRunner will need an instance of PlayerRepo.
	// Spring will inject an instance of PlayerRepository for this parameter
	public CommandLineRunner initData(PlayerRepository playerRepository, GameRepository gameRepository, GamePlayerRepository gamePlayerRepository) {
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


			GamePlayer gamePlayer1 = new GamePlayer(game1, player1);

			gamePlayerRepository.save(gamePlayer1);
		};
	}

}