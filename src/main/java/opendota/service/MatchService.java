package opendota.service;

import lombok.extern.slf4j.Slf4j;
import opendota.cache.EntityCache;
import opendota.model.Player;
import opendota.model.Match;
import opendota.repository.MatchRepository;
import opendota.repository.PlayerRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class MatchService {
    private final MatchRepository matchRepository;
    private final PlayerRepository playerRepository;
    private final EntityCache<Integer, Object> cacheMap;

    public MatchService(MatchRepository matchRepository, PlayerRepository playerRepository, EntityCache<Integer, Object> cacheMap) {
        this.matchRepository = matchRepository;
        this.playerRepository = playerRepository;
        this.cacheMap = cacheMap;
    }

    public Optional<Match> findMatchById(Long matchId) {
        int hashCode = Objects.hash(matchId, 32 * 33);
        Object cachedData = cacheMap.get(hashCode);

        if (cachedData != null) {
            log.info("match info is taken from the cache");
            return (Optional<Match>) cachedData;
        } else {
            Optional<Match> match = matchRepository.findById(matchId);
            cacheMap.put(hashCode, match);
            log.info("match info is taken from DB and placed into the cache");
            return match;
        }
    }

    public Match saveMatch(Match match) {
        cacheMap.clear();
        log.info("match info has been saved");
        return matchRepository.save(match);
    }

    public void deleteMatchById(Long matchId) {
        cacheMap.clear();
        log.info("information in the database has been deleted");
        matchRepository.deleteById(matchId);
    }

    public void updateMatch(Long matchId, Match updatedMatch) {
        cacheMap.clear();
        Optional<Match> matchOptional = matchRepository.findById(matchId);
        if (matchOptional.isPresent()) {
            Match match = matchOptional.get();
            match.setDuration(updatedMatch.getDuration());
            log.info("information in the database has been updated");
            matchRepository.save(match);
        }
    }

    public void addPlayer(Long matchId, Long playerId) {
        Player player = playerRepository.findById(playerId).orElseThrow();
        Match match = matchRepository.findById(matchId).orElseThrow(() -> new RuntimeException("match not found"));
        player.addMatch(match);
        match.addPlayer(player);
        log.info("player has been placed to matches");
        matchRepository.save(match);
    }

    public void removePlayerFromMatch(Long matchId, Long playerId) {
        cacheMap.clear();
        Optional<Match> matchOptional = matchRepository.findById(matchId);
        if (matchOptional.isPresent()) {
            Match match = matchOptional.get();
            Set<Player> players = match.getPlayers();
            players.removeIf(player -> player.getAccountId().equals(playerId));
            log.info("player has been removed from matches");
            matchRepository.save(match);
        }
    }
}

