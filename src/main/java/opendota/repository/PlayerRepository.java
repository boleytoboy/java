package opendota.repository;

import opendota.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PlayerRepository extends JpaRepository<Player, Long> {
    @Query("SELECT i FROM Player i WHERE i.personalName LIKE :prefix%")
    List<Player> findByBeginOfName(@Param("prefix") String prefix);
}
