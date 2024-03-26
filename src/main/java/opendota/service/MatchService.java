package opendota.service;

import opendota.model.Match;
import opendota.repository.MatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MatchService {
    private final MatchRepository matchRepository;

    public MatchService(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    public Optional<Match> findMatchById(Long matchId) {
        return matchRepository.findById(matchId);
    }

    public Match saveMatch(Match match) {
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
}

