package opendota.service;

import opendota.cache.EntityCache;
import opendota.model.Player;
import opendota.model.Match;
import opendota.repository.MatchRepository;
import opendota.repository.PlayerRepository;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class MatchService {
    private final MatchRepository matchRepository;
    private final PlayerRepository playerRepository;
    private final EntityCache<Integer,Object> cacheMap;

    public MatchService(MatchRepository matchRepository, PlayerRepository playerRepository, EntityCache<Integer, Object> cacheMap) {
        this.matchRepository = matchRepository;
        this.playerRepository = playerRepository;
        this.cacheMap = cacheMap;
    }
    public Optional<Match> findMatchById(Long matchId) {
        int hashCode = Objects.hash(matchId, 32 * 33);
        Object cachedData = cacheMap.get(hashCode);

        if (cachedData != null) {
            return (Optional<Match>) cachedData;
        } else {
            Optional<Match> match = matchRepository.findById(matchId);
            cacheMap.put(hashCode, match);
            return match;
        }
    }
    public Match saveMatch(Match match) {
        cacheMap.clear();
        return matchRepository.save(match);
    }
    public void deleteMatchById(Long matchId) {
        matchRepository.deleteById(matchId);
    }
    public void updateMatch(Long matchId, Match updatedMatch) {
        Optional<Match> matchOptional = matchRepository.findById(matchId);
        if (matchOptional.isPresent()) {
            Match match = matchOptional.get();
            match.setDuration(updatedMatch.getDuration());
            matchRepository.save(match);
        }
    }
    public void addPlayer(Long matchId, Long playerId) {
        Player player = playerRepository.findById(playerId).orElseThrow();
        Match match = matchRepository.findById(matchId).orElseThrow(() -> new RuntimeException("match not found"));
        player.addMatch(match);
        match.addPlayer(player);
        matchRepository.save(match);
    }
    public void removePlayerFromMatch(Long matchId, Long playerId) {
        Optional<Match> matchOptional = matchRepository.findById(matchId);
        if (matchOptional.isPresent()) {
            Match match = matchOptional.get();
            Set<Player> players = match.getPlayers();
            players.removeIf(player -> player.getAccountId().equals(playerId));
            matchRepository.save(match);
        }
    }
}

