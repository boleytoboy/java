package opendota.service;

import opendota.cache.EntityCache;
import opendota.model.Match;
import opendota.model.Player;
import opendota.repository.MatchRepository;
import opendota.repository.PlayerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MatchServiceTest {

    @Mock
    private MatchRepository matchRepository;

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private EntityCache<Integer, Object> cacheMap;

    @InjectMocks
    private MatchService matchService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void findMatchById_MatchFoundInCache_ShouldReturnMatchFromCache() {
        // Arrange
        Long matchId = 1L;
        Match match = new Match();
        int hashCode = Objects.hash(matchId, 32 * 33);
        when(cacheMap.get(hashCode)).thenReturn(Optional.of(match));

        // Act
        Optional<Match> result = matchService.findMatchById(matchId);

        // Assert
        assertEquals(Optional.of(match), result);
        verify(cacheMap, times(1)).get(hashCode);
        verify(matchRepository, never()).findById(matchId);
    }

    @Test
    void saveMatch_ShouldSaveMatchAndClearCache() {
        // Arrange
        Match match = new Match();

        // Act
        matchService.saveMatch(match);

        // Assert
        verify(matchRepository, times(1)).save(match);
        verify(cacheMap, times(1)).clear();
    }

    @Test
    void deleteMatchById_ShouldDeleteMatchAndClearCache() {
        // Arrange
        Long matchId = 1L;

        // Act
        matchService.deleteMatchById(matchId);

        // Assert
        verify(matchRepository, times(1)).deleteById(matchId);
        verify(cacheMap, times(1)).clear();
    }

    @Test
    void updateMatch_MatchExists_ShouldUpdateMatchAndClearCache() {
        // Arrange
        Long matchId = 1L;
        Match existingMatch = new Match();
        existingMatch.setMatchId(matchId);
        Match updatedMatch = new Match();
        updatedMatch.setMatchId(matchId);
        updatedMatch.setDuration(100);
        when(matchRepository.findById(matchId)).thenReturn(Optional.of(existingMatch));

        // Act
        matchService.updateMatch(matchId, updatedMatch);

        // Assert
        verify(matchRepository, times(1)).findById(matchId);
        verify(matchRepository, times(1)).save(existingMatch);
        assertEquals(updatedMatch.getDuration(), existingMatch.getDuration());
        verify(cacheMap, times(1)).clear();
    }

//    @Test
//    void addPlayer_PlayerAndMatchExist_ShouldAddPlayerToMatchAndSave() {
//        // Arrange
//        Long matchId = 1L;
//        Long playerId = 1L;
//        Player player = new Player();
//        Match match = new Match();
//        when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));
//        when(matchRepository.findById(matchId)).thenReturn(Optional.of(match));
//
//        // Act
//        //matchService.addPlayer(matchId, playerId);
//
//        // Assert
//       // assertTrue(match.getPlayers().contains(player));
//        //assertTrue(player.getMatches().contains(match));
//        verify(playerRepository, times(1)).findById(playerId);
//        verify(matchRepository, times(1)).findById(matchId);
//        verify(matchRepository, times(1)).save(match);
//    }

   // @Test
//    void removePlayerFromMatch_PlayerAndMatchExist_ShouldRemovePlayerFromMatchAndSave() {
//        // Arrange
//        Long matchId = 1L;
//        Long playerId = 1L;
//        Player player = new Player();
//        player.setAccountId(playerId);
//        Match match = new Match();
//        match.addPlayer(player);
//        when(matchRepository.findById(matchId)).thenReturn(Optional.of(match));
//
//        // Act
//        matchService.removePlayerFromMatch(matchId, playerId);
//
//        // Assert
//        assertFalse(match.getPlayers().contains(player));
//        verify(matchRepository, times(1)).findById(matchId);
//        verify(matchRepository, times(1)).save(match);
//        verify(cacheMap, times(1)).clear();
//    }
}
