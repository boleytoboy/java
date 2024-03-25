package opendota.service;

import opendota.entity.Match;
import opendota.repository.MatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MatchService {

    private final MatchRepository matchRepository;

    @Autowired
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

    public Match updateMatch(Long matchId, Match updatedMatch) {
        Optional<Match> matchOptional = matchRepository.findById(matchId);
        if (matchOptional.isPresent()) {
            Match match = matchOptional.get();
            match.setDuration(updatedMatch.getDuration());
            return matchRepository.save(match);
        } else {
            return null;
        }
    }

}

