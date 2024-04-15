package opendota.service;

import opendota.cache.EntityCache;
import opendota.model.Match;
import opendota.model.Player;
import opendota.repository.MatchRepository;
import opendota.repository.PlayerRepository;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class PlayerService {
    private final PlayerRepository playerRepository;
    private  final MatchRepository matchRepository;
    private final EntityCache<Integer,Object> cacheMap;
    public PlayerService(PlayerRepository playerRepository, MatchRepository matchRepository, EntityCache<Integer, Object> cacheMap) {
        this.playerRepository = playerRepository;
        this.matchRepository = matchRepository;
        this.cacheMap = cacheMap;
    }
    public Optional<Player> findPlayerById(Long accountId) {
        int hashCode = Objects.hash(accountId, 31 * 32);
        Object cachedData = cacheMap.get(hashCode);

        if (cachedData != null) {
            return (Optional<Player>) cachedData;
        } else {
            Optional<Player> player = playerRepository.findById(accountId);
            cacheMap.put(hashCode, player);
            return player;
        }
    }
    public List<Player> getPlayerByPrefix(String prefix) {
        int hashCode = Objects.hash(prefix, 34 * 35);
        Object cachedData = cacheMap.get(hashCode);

        if(cachedData != null) {
            return(List<Player>) cachedData;
        } else {
            cacheMap.put(hashCode, playerRepository.findByBeginOfName(prefix));
            return playerRepository.findByBeginOfName(prefix);
        }
    }
    public Player savePlayer(Player player) {
        cacheMap.clear();
        player.setAccountId(0L);
        return playerRepository.save(player);
    }
    public void deletePlayerById(Long accountId) {
        cacheMap.clear();
        Player player = playerRepository.findById(accountId).orElseThrow();
        for (Match match : player.getMatches()) {
            match.getPlayers().remove(player);
        }
        matchRepository.saveAll(player.getMatches());
        playerRepository.deleteById(accountId);
    }
    public void updatePlayer(Long accountId, Player updatedPlayer) {
        cacheMap.clear();
        Optional<Player> playerOptional = playerRepository.findById(accountId);
        if (playerOptional.isPresent()) {
            Player player = playerOptional.get();
            player.setPersonalName(updatedPlayer.getPersonalName());
            playerRepository.save(player);
        }
    }
}