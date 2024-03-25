package opendota.service;

import opendota.entity.Player;
import opendota.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;

    @Autowired
    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public Optional<Player> findPlayerById(Long accountId) {
        return playerRepository.findById(accountId);
    }

    public Player savePlayer(Player player) {
        return playerRepository.save(player);
    }

    public void deletePlayerById(Long accountId) {
        playerRepository.deleteById(accountId);
    }
}
