package opendota.controller;

import opendota.model.Player;
import opendota.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/players")
public class PlayerController {
    private final PlayerService playerService;

    @Autowired
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<Player>> createPlayers(@RequestBody List<Player> players) {
        List<Player> savedPlayers = playerService.savePlayers(players);
        return new ResponseEntity<>(savedPlayers, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Player> getPlayer(@PathVariable("id") Long accountId) {
        return playerService.findPlayerById(accountId)
                .map(player -> ResponseEntity.ok().body(player))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/prefix/{prefix}")
    public List<Player> getPlayerByPrefix(@PathVariable String prefix) {
        return playerService.getPlayerByPrefix(prefix);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlayer(@PathVariable("id") Long accountId) {
        playerService.deletePlayerById(accountId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updatePlayer(@PathVariable("id") Long accountId, @RequestBody Player player) {
        playerService.updatePlayer(accountId, player);
        return ResponseEntity.ok().build();
    }
}