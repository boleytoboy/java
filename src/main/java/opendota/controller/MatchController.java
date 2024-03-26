package opendota.controller;

import opendota.model.Match;
import opendota.service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/matches")
public class MatchController {
    private final MatchService matchService;
    @Autowired
    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }
    @PostMapping
    public ResponseEntity<Match> createMatch(@RequestBody Match match) {
        Match savedMatch = matchService.saveMatch(match);
        return new ResponseEntity<>(savedMatch, HttpStatus.CREATED);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Match> getMatch(@PathVariable("id") Long matchId) {
        return matchService.findMatchById(matchId)
                .map(match -> ResponseEntity.ok().body(match))
                .orElse(ResponseEntity.notFound().build());
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMatch(@PathVariable("id") Long matchId) {
        matchService.deleteMatchById(matchId);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateMatch(@PathVariable("id") Long matchId, @RequestBody Match match) {
        matchService.updateMatch(matchId, match);
        return ResponseEntity.noContent().build();
    }
}
