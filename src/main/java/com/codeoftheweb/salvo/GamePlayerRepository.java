package com.codeoftheweb.salvo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface GamePlayerRepository extends JpaRepository<GamePlayer, Long> {

    GamePlayer findById(@Param("id") long id);

}
