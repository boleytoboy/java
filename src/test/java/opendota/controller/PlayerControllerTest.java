package opendota.controller;

import opendota.model.Player;
import opendota.service.PlayerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PlayerControllerTest {

    @Mock
    private PlayerService playerService;

    @InjectMocks
    private PlayerController playerController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void createPlayers_ValidPlayers_ShouldReturnCreatedResponse() {
        // Arrange
        List<Player> players = Arrays.asList(new Player(), new Player());
        when(playerService.savePlayers(players)).thenReturn(players);

        // Act
        ResponseEntity<List<Player>> response = playerController.createPlayers(players);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(players, response.getBody());
    }

    @Test
    void getPlayer_ExistingPlayerId_ShouldReturnPlayer() {
        // Arrange
        Long accountId = 1L;
        Player player = new Player();
        when(playerService.findPlayerById(accountId)).thenReturn(Optional.of(player));

        // Act
        ResponseEntity<Player> response = playerController.getPlayer(accountId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(player, response.getBody());
    }

    @Test
    void getPlayerByPrefix_ValidPrefix_ShouldReturnListOfPlayers() {
        // Arrange
        String prefix = "prefix";
        List<Player> players = Arrays.asList(new Player(), new Player());
        when(playerService.getPlayerByPrefix(prefix)).thenReturn(players);

        // Act
        List<Player> result = playerController.getPlayerByPrefix(prefix);

        // Assert
        assertEquals(players, result);
    }

    @Test
    void deletePlayer_ExistingPlayerId_ShouldReturnOkResponse() {
        // Arrange
        Long accountId = 1L;

        // Act
        ResponseEntity<Void> response = playerController.deletePlayer(accountId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(playerService, times(1)).deletePlayerById(accountId);
    }

    @Test
    void updatePlayer_ExistingPlayerId_ShouldReturnOkResponse() {
        // Arrange
        Long accountId = 1L;
        Player player = new Player();

        // Act
        ResponseEntity<Void> response = playerController.updatePlayer(accountId, player);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(playerService, times(1)).updatePlayer(accountId, player);
    }
}
