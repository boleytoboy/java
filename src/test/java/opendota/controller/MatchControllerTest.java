package opendota.controller;

import opendota.model.Match;
import opendota.service.MatchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class MatchControllerTest {

    @Mock
    private MatchService matchService;

    @InjectMocks
    private MatchController matchController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void createMatch_ValidMatch_ShouldReturnCreatedResponse() {
        // Arrange
        Match match = new Match();
        when(matchService.saveMatch(any())).thenReturn(match);

        // Act
        ResponseEntity<Match> response = matchController.createMatch(match);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(match, response.getBody());
    }

    @Test
    void getMatch_ExistingMatchId_ShouldReturnMatch() {
        // Arrange
        Long matchId = 1L;
        Match match = new Match();
        when(matchService.findMatchById(matchId)).thenReturn(Optional.of(match));

        // Act
        ResponseEntity<Match> response = matchController.getMatch(matchId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(match, response.getBody());
    }

    @Test
    void deleteMatch_ExistingMatchId_ShouldReturnOkResponse() {
        // Arrange
        Long matchId = 1L;

        // Act
        ResponseEntity<Void> response = matchController.deleteMatch(matchId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(matchService, times(1)).deleteMatchById(matchId);
    }

    @Test
    void updateMatch_ExistingMatchId_ShouldReturnOkResponse() {
        // Arrange
        Long matchId = 1L;
        Match match = new Match();

        // Act
        ResponseEntity<Void> response = matchController.updateMatch(matchId, match);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(matchService, times(1)).updateMatch(matchId, match);
    }

    @Test
    void addPlayer_ValidMatchAndPlayerId_ShouldReturnOkResponse() {
        // Arrange
        Long matchId = 1L;
        Long playerId = 1L;

        // Act
        ResponseEntity<Void> response = matchController.addPlayer(matchId, playerId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(matchService, times(1)).addPlayer(matchId, playerId);
    }

    @Test
    void removePlayerFromMatch_ValidMatchAndPlayerId_ShouldReturnOkResponse() {
        // Arrange
        Long matchId = 1L;
        Long playerId = 1L;

        // Act
        ResponseEntity<Void> response = matchController.removePlayerFromMatch(matchId, playerId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(matchService, times(1)).removePlayerFromMatch(matchId, playerId);
    }
}
