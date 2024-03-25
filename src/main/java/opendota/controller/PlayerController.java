package opendota.controller;

import opendota.entity.Player;
import opendota.service.PlayerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/players")
public class PlayerController {

    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @PostMapping
    public ResponseEntity<Player> createPlayer(@RequestBody Player player) {
        Player savedPlayer = playerService.savePlayer(player);
        return new ResponseEntity<>(savedPlayer, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Player> getPlayer(@PathVariable("id") Long accountId) {
        return playerService.findPlayerById(accountId)
                .map(player -> ResponseEntity.ok().body(player))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlayer(@PathVariable("id") Long accountId) {
        playerService.deletePlayerById(accountId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updatePlayer(@PathVariable("id") Long accountId, @RequestBody Player player) {
        // Поиск игрока по accountId
        Optional<Player> existingPlayerOptional = playerService.findPlayerById(accountId);

        // Проверка, существует ли игрок с указанным accountId
        if (existingPlayerOptional.isPresent()) {
            // Получение существующего игрока
            Player existingPlayer = existingPlayerOptional.get();

            // Установка нового имени игрока из запроса
            existingPlayer.setPersonalName(player.getPersonalName());

            // Сохранение обновленного игрока в базе данных
            playerService.savePlayer(existingPlayer);

            // Возврат ответа об успешном выполнении запроса
            return ResponseEntity.noContent().build();
        } else {
            // Если игрок с указанным accountId не найден, вернуть ошибку 404
            return ResponseEntity.notFound().build();
        }
    }


}
