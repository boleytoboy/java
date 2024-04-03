package opendota.repository;

import opendota.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PlayerRepository extends JpaRepository<Player, Long> {
    @Query("SELECT p FROM Player p")
    List<Player> findAllPlayers();
}
