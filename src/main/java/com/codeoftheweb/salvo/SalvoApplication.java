package com.codeoftheweb.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
    public CommandLineRunner initData(PlayerRepository playerRepository, GameRepository gameRepository, GamePlayerRepository gamePlayerRepository, ShipRepository shipRepository, SalvoRepository salvoRepository, ScoreRepository scoreRepository) {
        return (args) -> {

            Player player1 = new Player("jack@gmail.com", "kjsdkjf");
            Player player2 = new Player("chloe@gmail.com", "kjhdfj2");
            Player player3 = new Player("kim@gmail.com", "bvkraf34");
            Player player4 = new Player("david@gmail.com", "vwhthb");
            Player player5 = new Player("michaelle@gmail.com", "1");

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
            GamePlayer gamePlayer2 = new GamePlayer(game3, player2); // Chloe
            GamePlayer gamePlayer3 = new GamePlayer(game2, player1); // Jack
            GamePlayer gamePlayer4 = new GamePlayer(game1, player5); //Michelle
            GamePlayer gamePlayer5 = new GamePlayer(game1, player3); // Kim
            GamePlayer gamePlayer6 = new GamePlayer(game2, player4); // David

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

            Score score1 = new Score(0.5, game1, player5);
            Score score5 = new Score(0.5, game1, player3);

            Score score2 = new Score(0, game2, player1);
            Score score6 = new Score(1, game2, player4);
            Score score7 = new Score(1, game3, player5);

	/*		Score score3 = new Score(0.5, game3, player5);
			Score score4 = new Score(0.5, game3, player2);*/

            gamePlayerRepository.save(gamePlayer1);
            gamePlayerRepository.save(gamePlayer2);
            gamePlayerRepository.save(gamePlayer3);
            gamePlayerRepository.save(gamePlayer4);
            gamePlayerRepository.save(gamePlayer5);
            gamePlayerRepository.save(gamePlayer6);

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

            scoreRepository.save(score1);
            scoreRepository.save(score2);
		/*	scoreRepository.save(score3);
			scoreRepository.save(score4);*/
            scoreRepository.save(score5);
            scoreRepository.save(score6);
            scoreRepository.save(score7);


        };
    }

}

@Configuration
class WebSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter {
    @Autowired
    PlayerRepository playerRepository;

    @Override
    public void init(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(inputName -> {
            System.out.println(inputName);
            Player player = playerRepository.findByEmail(inputName);
//            System.out.println(player);
            if (player != null) {
                return User.withDefaultPasswordEncoder()
                        .username(player.getEmail())
                        .password(player.getPassword())
                        .roles("USER")
                        .build();
            } else {
                throw new UsernameNotFoundException("Unknown user: " + inputName);
            }
        });
    }
}

@EnableWebSecurity
@Configuration
class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()

                .antMatchers("/web/games.html").permitAll()
                .antMatchers("/web/games.css").permitAll()
                .antMatchers("/api/games").permitAll()
                .antMatchers("/web/games1.js").permitAll()

                .antMatchers("/favicon.ico").permitAll()
                .antMatchers("/web/login.html").permitAll()
                .antMatchers("/web/login.js").permitAll()


                .antMatchers("/api/leaderboard").permitAll()
                .antMatchers("/api/games/players/*/ships").permitAll()
                .antMatchers("/api/games/players/*/salvoes").permitAll()
                .antMatchers("/api/players").permitAll()
                .antMatchers("/web/game.html").authenticated()
                .antMatchers("/web/game.js").authenticated()
                .antMatchers("/web/game.css").authenticated()
                .antMatchers("/api/game_view/*").authenticated();



        http.formLogin()
                .usernameParameter("email")
                .passwordParameter("password");
//                .loginPage("/api/login");

        http.logout().logoutUrl("/api/logout");
        // turn off checking for CSRF tokens
        http.csrf().disable();

        // if user is not authenticated, just send an authentication failure response
        http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // if login is successful, just clear the flags asking for authentication
        http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

        // if login fails, just send an authentication failure response
        http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // if logout is successful, just send a success response
        http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
    }

    private void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }
    }

}