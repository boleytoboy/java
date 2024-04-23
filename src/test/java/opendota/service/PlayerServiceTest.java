package opendota.service;

import opendota.cache.EntityCache;
import opendota.model.Player;
import opendota.repository.MatchRepository;
import opendota.repository.PlayerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PlayerServiceTest {

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private MatchRepository matchRepository;

    @Mock
    private EntityCache<Integer, Object> cacheMap;

    @InjectMocks
    private PlayerService playerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void findPlayerById_PlayerFoundInCache_ShouldReturnPlayerFromCache() {
        // Arrange
        Long accountId = 1L;
        Player player = new Player();
        int hashCode = Objects.hash(accountId, 31 * 32);
        when(cacheMap.get(hashCode)).thenReturn(Optional.of(player));

        // Act
        Optional<Player> result = playerService.findPlayerById(accountId);

        // Assert
        assertEquals(Optional.of(player), result);
        verify(cacheMap, times(1)).get(hashCode);
        verify(playerRepository, never()).findById(accountId);
    }

    @Test
    void getPlayerByPrefix_PlayersFoundInCache_ShouldReturnPlayersFromCache() {
        // Arrange
        String prefix = "test";
        List<Player> players = new ArrayList<>();
        players.add(new Player());
        players.add(new Player());
        int hashCode = Objects.hash(prefix, 34 * 35);
        when(cacheMap.get(hashCode)).thenReturn(players);

        // Act
        List<Player> result = playerService.getPlayerByPrefix(prefix);

        // Assert
        assertEquals(players, result);
        verify(cacheMap, times(1)).get(hashCode);
        verify(playerRepository, never()).findByBeginOfName(prefix);
    }

    @Test
    void savePlayers_ShouldSavePlayersAndClearCache() {
        // Arrange
        List<Player> players = new ArrayList<>();
        Player player1 = new Player();
        Player player2 = new Player();
        players.add(player1);
        players.add(player2);

        // Act
        playerService.savePlayers(players);

        // Assert
        verify(playerRepository, times(1)).saveAll(players);
        verify(cacheMap, times(1)).clear();
        assertEquals(0L, player1.getAccountId());
        assertEquals(0L, player2.getAccountId());
    }

    @Test
    void deletePlayerById_ShouldDeletePlayerAndClearCache() {
        // Arrange
        Long accountId = 1L;

        // Act
        playerService.deletePlayerById(accountId);

        // Assert
        verify(playerRepository, times(1)).findById(accountId);
        verify(matchRepository, times(1)).saveAll(any());
        verify(playerRepository, times(1)).deleteById(accountId);
        verify(cacheMap, times(1)).clear();
    }

    @Test
    void updatePlayer_PlayerExists_ShouldUpdatePlayerAndClearCache() {
        // Arrange
        Long accountId = 1L;
        Player existingPlayer = new Player();
        existingPlayer.setAccountId(accountId);
        Player updatedPlayer = new Player();
        updatedPlayer.setAccountId(accountId);
        updatedPlayer.setPersonalName("Updated Name");
        when(playerRepository.findById(accountId)).thenReturn(Optional.of(existingPlayer));

        // Act
        playerService.updatePlayer(accountId, updatedPlayer);

        // Assert
        verify(playerRepository, times(1)).findById(accountId);
        verify(playerRepository, times(1)).save(existingPlayer);
        assertEquals(updatedPlayer.getPersonalName(), existingPlayer.getPersonalName());
        verify(cacheMap, times(1)).clear();
    }
}
