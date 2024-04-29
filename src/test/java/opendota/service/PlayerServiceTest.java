package opendota.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import opendota.cache.EntityCache;
import opendota.model.Category;
import opendota.model.Match;
import opendota.model.Player;
import opendota.repository.MatchRepository;
import opendota.repository.PlayerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {PlayerService.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class PlayerServiceTest {
    @MockBean
    private EntityCache<Integer, Object> entityCache;

    @MockBean
    private MatchRepository matchRepository;

    @MockBean
    private PlayerRepository playerRepository;

    @Autowired
    private PlayerService playerService;

    /**
     * Method under test: {@link PlayerService#findPlayerById(Long)}
     */
    @Test
    void testFindPlayerById() {
        // Arrange
        Player player = mock(Player.class);
        doNothing().when(player).setAccountId(Mockito.<Long>any());
        doNothing().when(player).setMatches(Mockito.<Set<Match>>any());
        doNothing().when(player).setPersonalName(Mockito.<String>any());
        player.setAccountId(1L);
        player.setMatches(new HashSet<>());
        player.setPersonalName("Personal Name");
        Optional.of(player);
        Optional<Object> ofResult = Optional.of("42");
        when(entityCache.get(Mockito.<Integer>any())).thenReturn(ofResult);

        // Act
        Optional<Player> actualFindPlayerByIdResult = playerService.findPlayerById(1L);

        // Assert
        verify(entityCache).get(1984);
        verify(player).setAccountId(1L);
        verify(player).setMatches(isA(Set.class));
        verify(player).setPersonalName("Personal name");
        assertSame(ofResult, actualFindPlayerByIdResult);
    }

    /**
     * Method under test: {@link PlayerService#findPlayerById(Long)}
     */
    @Test
    void testFindPlayerById2() {
        // Arrange
        Player player = mock(Player.class);
        doNothing().when(player).setAccountId(Mockito.<Long>any());
        doNothing().when(player).setMatches(Mockito.<Set<Match>>any());
        doNothing().when(player).setPersonalName(Mockito.<String>any());
        player.setAccountId(1L);
        player.setMatches(new HashSet<>());
        player.setPersonalName("Personal Name");
        Optional<Player> ofResult = Optional.of(player);
        when(playerRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        when(entityCache.get(Mockito.<Integer>any())).thenReturn(null);
        doNothing().when(entityCache).put(Mockito.<Integer>any(), Mockito.<Object>any());

        // Act
        Optional<Player> actualFindPlayerByIdResult = playerService.findPlayerById(1L);

        // Assert
        verify(entityCache).get(1984);
        verify(entityCache).put(eq(1984), isA(Object.class));
        verify(player).setAccountId(1L);
        verify(player).setMatches(isA(Set.class));
        verify(player).setPersonalName("Personal name");
        verify(playerRepository).findById(1L);
        assertSame(ofResult, actualFindPlayerByIdResult);
    }

    /**
     * Method under test: {@link PlayerService#getPlayerByPrefix(String)}
     */
    @Test
    void testGetPlayerByPrefix() {
        // Arrange
        ArrayList<Object> objectList = new ArrayList<>();
        when(entityCache.get(Mockito.<Integer>any())).thenReturn(objectList);

        // Act
        List<Player> actualPlayerByPrefix = playerService.getPlayerByPrefix("Prefix");

        // Assert
        verify(entityCache).get(1345994741);
        assertTrue(actualPlayerByPrefix.isEmpty());
        assertSame(objectList, actualPlayerByPrefix);
    }

    /**
     * Method under test: {@link PlayerService#getPlayerByPrefix(String)}
     */
    @Test
    void testGetPlayerByPrefix2() {
        // Arrange
        ArrayList<Player> playerList = new ArrayList<>();
        when(playerRepository.findByBeginOfName(Mockito.<String>any())).thenReturn(playerList);
        when(entityCache.get(Mockito.<Integer>any())).thenReturn(null);
        doNothing().when(entityCache).put(Mockito.<Integer>any(), Mockito.<Object>any());

        // Act
        List<Player> actualPlayerByPrefix = playerService.getPlayerByPrefix("Prefix");

        // Assert
        verify(entityCache).get(1345994741);
        verify(entityCache).put(eq(1345994741), isA(Object.class));
        verify(playerRepository, atLeast(1)).findByBeginOfName("Prefix");
        assertTrue(actualPlayerByPrefix.isEmpty());
        assertSame(playerList, actualPlayerByPrefix);
    }

    /**
     * Method under test: {@link PlayerService#savePlayers(List)}
     */
    @Test
    void testSavePlayers() {
        // Arrange
        ArrayList<Player> playerList = new ArrayList<>();
        when(playerRepository.saveAll(Mockito.<Iterable<Player>>any())).thenReturn(playerList);
        doNothing().when(entityCache).clear();

        // Act
        List<Player> actualSavePlayersResult = playerService.savePlayers(new ArrayList<>());

        // Assert
        verify(entityCache).clear();
        verify(playerRepository).saveAll(isA(Iterable.class));
        assertTrue(actualSavePlayersResult.isEmpty());
        assertSame(playerList, actualSavePlayersResult);
    }

    /**
     * Method under test: {@link PlayerService#savePlayers(List)}
     */
    @Test
    void testSavePlayers2() {
        // Arrange
        ArrayList<Player> playerList = new ArrayList<>();
        when(playerRepository.saveAll(Mockito.<Iterable<Player>>any())).thenReturn(playerList);
        doNothing().when(entityCache).clear();

        Player player = new Player();
        player.setAccountId(1L);
        player.setMatches(new HashSet<>());
        player.setPersonalName("players info has been saved");

        ArrayList<Player> players = new ArrayList<>();
        players.add(player);

        // Act
        List<Player> actualSavePlayersResult = playerService.savePlayers(players);

        // Assert
        verify(entityCache).clear();
        verify(playerRepository).saveAll(isA(Iterable.class));
        assertEquals(0L, players.get(0).getAccountId().longValue());
        assertTrue(actualSavePlayersResult.isEmpty());
        assertSame(playerList, actualSavePlayersResult);
    }

    /**
     * Method under test: {@link PlayerService#savePlayers(List)}
     */
    @Test
    void testSavePlayers3() {
        // Arrange
        ArrayList<Player> playerList = new ArrayList<>();
        when(playerRepository.saveAll(Mockito.<Iterable<Player>>any())).thenReturn(playerList);
        doNothing().when(entityCache).clear();

        Player player = new Player();
        player.setAccountId(1L);
        player.setMatches(new HashSet<>());
        player.setPersonalName("players info has been saved");

        Player player2 = new Player();
        player2.setAccountId(2L);
        player2.setMatches(new HashSet<>());
        player2.setPersonalName("Personal Name");

        ArrayList<Player> players = new ArrayList<>();
        players.add(player2);
        players.add(player);

        // Act
        List<Player> actualSavePlayersResult = playerService.savePlayers(players);

        // Assert
        verify(entityCache).clear();
        verify(playerRepository).saveAll(isA(Iterable.class));
        assertEquals(0L, players.get(0).getAccountId().longValue());
        assertEquals(0L, players.get(1).getAccountId().longValue());
        assertTrue(actualSavePlayersResult.isEmpty());
        assertSame(playerList, actualSavePlayersResult);
    }

    /**
     * Method under test: {@link PlayerService#deletePlayerById(Long)}
     */
    @Test
    void testDeletePlayerById() {
        // Arrange
        Player player = new Player();
        player.setAccountId(1L);
        player.setMatches(new HashSet<>());
        player.setPersonalName("Personal Name");
        Optional<Player> ofResult = Optional.of(player);
        doNothing().when(playerRepository).deleteById(Mockito.<Long>any());
        when(playerRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        when(matchRepository.saveAll(Mockito.<Iterable<Match>>any())).thenReturn(new ArrayList<>());
        doNothing().when(entityCache).clear();

        // Act
        playerService.deletePlayerById(1L);

        // Assert that nothing has changed
        verify(entityCache).clear();
        verify(playerRepository).deleteById(1L);
        verify(playerRepository).findById(1L);
        verify(matchRepository).saveAll(isA(Iterable.class));
    }

    /**
     * Method under test: {@link PlayerService#deletePlayerById(Long)}
     */
    @Test
    void testDeletePlayerById2() {
        // Arrange
        Category category = new Category();
        category.setCategoryId(1L);
        category.setMatches(new HashSet<>());
        category.setName("information in the database has been deleted");

        Match match = new Match();
        match.setCategory(category);
        match.setDuration(1);
        match.setMatchId(1L);
        match.setPlayers(new HashSet<>());

        HashSet<Match> matches = new HashSet<>();
        matches.add(match);

        Player player = new Player();
        player.setAccountId(1L);
        player.setMatches(matches);
        player.setPersonalName("Personal Name");
        Optional<Player> ofResult = Optional.of(player);
        doNothing().when(playerRepository).deleteById(Mockito.<Long>any());
        when(playerRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        when(matchRepository.saveAll(Mockito.<Iterable<Match>>any())).thenReturn(new ArrayList<>());
        doNothing().when(entityCache).clear();

        // Act
        playerService.deletePlayerById(1L);

        // Assert
        verify(entityCache).clear();
        verify(playerRepository).deleteById(1L);
        verify(playerRepository).findById(1L);
        verify(matchRepository).saveAll(isA(Iterable.class));
    }

    /**
     * Method under test: {@link PlayerService#updatePlayer(Long, Player)}
     */
    @Test
    void testUpdatePlayer() {
        // Arrange
        Player player = new Player();
        player.setAccountId(1L);
        player.setMatches(new HashSet<>());
        player.setPersonalName("Personal Name");
        Optional<Player> ofResult = Optional.of(player);

        Player player2 = new Player();
        player2.setAccountId(1L);
        player2.setMatches(new HashSet<>());
        player2.setPersonalName("Personal Name");
        when(playerRepository.save(Mockito.<Player>any())).thenReturn(player2);
        when(playerRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        doNothing().when(entityCache).clear();

        Player updatedPlayer = new Player();
        updatedPlayer.setAccountId(1L);
        updatedPlayer.setMatches(new HashSet<>());
        updatedPlayer.setPersonalName("Personal Name");

        // Act
        playerService.updatePlayer(1L, updatedPlayer);

        // Assert
        verify(entityCache).clear();
        verify(playerRepository).findById(1L);
        verify(playerRepository).save(isA(Player.class));
    }

    /**
     * Method under test: {@link PlayerService#updatePlayer(Long, Player)}
     */
    @Test
    void testUpdatePlayer2() {
        // Arrange
        Optional<Player> emptyResult = Optional.empty();
        when(playerRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);
        doNothing().when(entityCache).clear();

        Player updatedPlayer = new Player();
        updatedPlayer.setAccountId(1L);
        updatedPlayer.setMatches(new HashSet<>());
        updatedPlayer.setPersonalName("Personal Name");

        // Act
        playerService.updatePlayer(1L, updatedPlayer);

        // Assert that nothing has changed
        verify(entityCache).clear();
        verify(playerRepository).findById(1L);
        assertEquals("Personal Name", updatedPlayer.getPersonalName());
        assertEquals(1L, updatedPlayer.getAccountId().longValue());
        assertTrue(updatedPlayer.getMatches().isEmpty());
    }
}
