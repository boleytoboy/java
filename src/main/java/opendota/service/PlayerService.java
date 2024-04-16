package opendota.service;

import lombok.extern.slf4j.Slf4j;
import opendota.cache.EntityCache;
import opendota.model.Match;
import opendota.model.Player;
import opendota.repository.MatchRepository;
import opendota.repository.PlayerRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class PlayerService {
    private final PlayerRepository playerRepository;
    private final MatchRepository matchRepository;
    private final EntityCache<Integer, Object> cacheMap;

    public PlayerService(PlayerRepository playerRepository, MatchRepository matchRepository, EntityCache<Integer, Object> cacheMap) {
        this.playerRepository = playerRepository;
        this.matchRepository = matchRepository;
        this.cacheMap = cacheMap;
    }

    public Optional<Player> findPlayerById(Long accountId) {
        int hashCode = Objects.hash(accountId, 31 * 32);
        Object cachedData = cacheMap.get(hashCode);

        if (cachedData != null) {
            log.info("player info is taken from the cache");
            return (Optional<Player>) cachedData;
        } else {
            Optional<Player> player = playerRepository.findById(accountId);
            cacheMap.put(hashCode, player);
            log.info("player info is taken from DB and placed into the cache");
            return player;
        }
    }

    public List<Player> getPlayerByPrefix(String prefix) {
        int hashCode = Objects.hash(prefix, 34 * 35);
        Object cachedData = cacheMap.get(hashCode);

        if (cachedData != null) {
            log.info("player info is taken from the cache");
            return (List<Player>) cachedData;
        } else {
            cacheMap.put(hashCode, playerRepository.findByBeginOfName(prefix));
            log.info("player info is taken from DB and placed into the cache");
            return playerRepository.findByBeginOfName(prefix);
        }
    }

    public Player savePlayer(Player player) {
        cacheMap.clear();
        player.setAccountId(0L);
        log.info("player info has been saved");
        return playerRepository.save(player);
    }

    public void deletePlayerById(Long accountId) {
        cacheMap.clear();
        Player player = playerRepository.findById(accountId).orElseThrow();
        for (Match match : player.getMatches()) {
            match.getPlayers().remove(player);
        }
        log.info("information in the database has been deleted");
        matchRepository.saveAll(player.getMatches());
        playerRepository.deleteById(accountId);
    }

    public void updatePlayer(Long accountId, Player updatedPlayer) {
        cacheMap.clear();
        Optional<Player> playerOptional = playerRepository.findById(accountId);
        if (playerOptional.isPresent()) {
            Player player = playerOptional.get();
            player.setPersonalName(updatedPlayer.getPersonalName());
            log.info("information in the database has been updated");
            playerRepository.save(player);
        }
    }
}