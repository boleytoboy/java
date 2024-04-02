package opendota.service;

import opendota.model.Player;
import opendota.repository.PlayerRepository;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class PlayerService {
    private final PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public Optional<Player> findPlayerById(Long accountId) {
        return playerRepository.findById(accountId);
    }

    public Player savePlayer(Player player) {
        player.setAccountId(0L);
        return playerRepository.save(player);
    }

    public void deletePlayerById(Long accountId) {
        playerRepository.deleteById(accountId);
    }
    public void updatePlayer(Long accountId, Player updatedPlayer) {
        Optional<Player> playerOptional = playerRepository.findById(accountId);
        if (playerOptional.isPresent()) {
            Player player = playerOptional.get();
            player.setPersonalName(updatedPlayer.getPersonalName());
            playerRepository.save(player);
        }
    }

}
