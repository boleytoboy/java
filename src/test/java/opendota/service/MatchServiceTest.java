package opendota.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
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

@ContextConfiguration(classes = {MatchService.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class MatchServiceTest {
    @MockBean
    private EntityCache<Integer, Object> entityCache;

    @MockBean
    private MatchRepository matchRepository;

    @Autowired
    private MatchService matchService;

    @MockBean
    private PlayerRepository playerRepository;

    /**
     * Method under test: {@link MatchService#findMatchById(Long)}
     */
    @Test
    void testFindMatchById() {
        // Arrange
        when(entityCache.get(Mockito.<Integer>any()))
                .thenThrow(new RuntimeException("match info is taken from DB and placed into the cache"));

        // Act and Assert
        assertThrows(RuntimeException.class, () -> matchService.findMatchById(1L));
        verify(entityCache).get(2048);
    }

    /**
     * Method under test: {@link MatchService#findMatchById(Long)}
     */
    @Test
    void testFindMatchById2() {
        // Arrange
        Category category = mock(Category.class);
        doNothing().when(category).setCategoryId(Mockito.<Long>any());
        doNothing().when(category).setMatches(Mockito.<Set<Match>>any());
        doNothing().when(category).setName(Mockito.<String>any());
        category.setCategoryId(1L);
        category.setMatches(new HashSet<>());
        category.setName("Name");
        Match match = mock(Match.class);
        doNothing().when(match).setCategory(Mockito.<Category>any());
        doNothing().when(match).setDuration(Mockito.<Integer>any());
        doNothing().when(match).setMatchId(Mockito.<Long>any());
        doNothing().when(match).setPlayers(Mockito.<Set<Player>>any());
        match.setCategory(category);
        match.setDuration(1);
        match.setMatchId(1L);
        match.setPlayers(new HashSet<>());
        Optional.of(match);
        Optional<Object> ofResult = Optional.of("42");
        when(entityCache.get(Mockito.<Integer>any())).thenReturn(ofResult);

        // Act
        Optional<Match> actualFindMatchByIdResult = matchService.findMatchById(1L);

        // Assert
        verify(entityCache).get(2048);
        verify(category).setCategoryId(1L);
        verify(category).setMatches(isA(Set.class));
        verify(category).setName("Name");
        verify(match).setCategory(isA(Category.class));
        verify(match).setDuration(1);
        verify(match).setMatchId(1L);
        verify(match).setPlayers(isA(Set.class));
        assertSame(ofResult, actualFindMatchByIdResult);
    }

    /**
     * Method under test: {@link MatchService#findMatchById(Long)}
     */
    @Test
    void testFindMatchById3() {
        // Arrange
        Category category = mock(Category.class);
        doNothing().when(category).setCategoryId(Mockito.<Long>any());
        doNothing().when(category).setMatches(Mockito.<Set<Match>>any());
        doNothing().when(category).setName(Mockito.<String>any());
        category.setCategoryId(1L);
        category.setMatches(new HashSet<>());
        category.setName("Name");
        Match match = mock(Match.class);
        doNothing().when(match).setCategory(Mockito.<Category>any());
        doNothing().when(match).setDuration(Mockito.<Integer>any());
        doNothing().when(match).setMatchId(Mockito.<Long>any());
        doNothing().when(match).setPlayers(Mockito.<Set<Player>>any());
        match.setCategory(category);
        match.setDuration(1);
        match.setMatchId(1L);
        match.setPlayers(new HashSet<>());
        Optional<Match> ofResult = Optional.of(match);
        when(matchRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        when(entityCache.get(Mockito.<Integer>any())).thenReturn(null);
        doNothing().when(entityCache).put(Mockito.<Integer>any(), Mockito.<Object>any());

        // Act
        Optional<Match> actualFindMatchByIdResult = matchService.findMatchById(1L);

        // Assert
        verify(entityCache).get(2048);
        verify(entityCache).put(2048, isA(Object.class));
        verify(category).setCategoryId(1L);
        verify(category).setMatches(isA(Set.class));
        verify(category).setName("Name");
        verify(match).setCategory(isA(Category.class));
        verify(match).setDuration(1);
        verify(match).setMatchId(1L);
        verify(match).setPlayers(isA(Set.class));
        verify(matchRepository).findById(1L);
        assertSame(ofResult, actualFindMatchByIdResult);
    }

    /**
     * Method under test: {@link MatchService#saveMatch(Match)}
     */
    @Test
    void testSaveMatch() {
        // Arrange
        Category category = new Category();
        category.setCategoryId(1L);
        category.setMatches(new HashSet<>());
        category.setName("Name");

        Match match = new Match();
        match.setCategory(category);
        match.setDuration(1);
        match.setMatchId(1L);
        match.setPlayers(new HashSet<>());
        when(matchRepository.save(Mockito.<Match>any())).thenReturn(match);
        doNothing().when(entityCache).clear();

        Category category2 = new Category();
        category2.setCategoryId(1L);
        category2.setMatches(new HashSet<>());
        category2.setName("Name");

        Match match2 = new Match();
        match2.setCategory(category2);
        match2.setDuration(1);
        match2.setMatchId(1L);
        match2.setPlayers(new HashSet<>());

        // Act
        Match actualSaveMatchResult = matchService.saveMatch(match2);

        // Assert
        verify(entityCache).clear();
        verify(matchRepository).save(isA(Match.class));
        assertSame(match, actualSaveMatchResult);
    }

    /**
     * Method under test: {@link MatchService#saveMatch(Match)}
     */
    @Test
    void testSaveMatch2() {
        // Arrange
        doThrow(new RuntimeException("match info has been saved")).when(entityCache).clear();

        Category category = new Category();
        category.setCategoryId(1L);
        category.setMatches(new HashSet<>());
        category.setName("Name");

        Match match = new Match();
        match.setCategory(category);
        match.setDuration(1);
        match.setMatchId(1L);
        match.setPlayers(new HashSet<>());

        // Act and Assert
        assertThrows(RuntimeException.class, () -> matchService.saveMatch(match));
        verify(entityCache).clear();
    }

    /**
     * Method under test: {@link MatchService#deleteMatchById(Long)}
     */
    @Test
    void testDeleteMatchById() {
        // Arrange
        doNothing().when(matchRepository).deleteById(Mockito.<Long>any());
        doNothing().when(entityCache).clear();

        // Act
        matchService.deleteMatchById(1L);

        // Assert that nothing has changed
        verify(entityCache).clear();
        verify(matchRepository).deleteById(1L);
    }

    /**
     * Method under test: {@link MatchService#deleteMatchById(Long)}
     */
    @Test
    void testDeleteMatchById2() {
        // Arrange
        doThrow(new RuntimeException("information in the database has been deleted")).when(entityCache).clear();

        // Act and Assert
        assertThrows(RuntimeException.class, () -> matchService.deleteMatchById(1L));
        verify(entityCache).clear();
    }

    /**
     * Method under test: {@link MatchService#updateMatch(Long, Match)}
     */
    @Test
    void testUpdateMatch() {
        // Arrange
        Category category = new Category();
        category.setCategoryId(1L);
        category.setMatches(new HashSet<>());
        category.setName("Name");

        Match match = new Match();
        match.setCategory(category);
        match.setDuration(1);
        match.setMatchId(1L);
        match.setPlayers(new HashSet<>());
        Optional<Match> ofResult = Optional.of(match);

        Category category2 = new Category();
        category2.setCategoryId(1L);
        category2.setMatches(new HashSet<>());
        category2.setName("Name");

        Match match2 = new Match();
        match2.setCategory(category2);
        match2.setDuration(1);
        match2.setMatchId(1L);
        match2.setPlayers(new HashSet<>());
        when(matchRepository.save(Mockito.<Match>any())).thenReturn(match2);
        when(matchRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        doNothing().when(entityCache).clear();

        Category category3 = new Category();
        category3.setCategoryId(1L);
        category3.setMatches(new HashSet<>());
        category3.setName("Name");

        Match updatedMatch = new Match();
        updatedMatch.setCategory(category3);
        updatedMatch.setDuration(1);
        updatedMatch.setMatchId(1L);
        updatedMatch.setPlayers(new HashSet<>());

        // Act
        matchService.updateMatch(1L, updatedMatch);

        // Assert
        verify(entityCache).clear();
        verify(matchRepository).findById(1L);
        verify(matchRepository).save(isA(Match.class));
    }

    /**
     * Method under test: {@link MatchService#updateMatch(Long, Match)}
     */
    @Test
    void testUpdateMatch2() {
        // Arrange
        doThrow(new RuntimeException("information in the database has been updated")).when(entityCache).clear();

        Category category = new Category();
        category.setCategoryId(1L);
        category.setMatches(new HashSet<>());
        category.setName("Name");

        Match updatedMatch = new Match();
        updatedMatch.setCategory(category);
        updatedMatch.setDuration(1);
        updatedMatch.setMatchId(1L);
        updatedMatch.setPlayers(new HashSet<>());

        // Act and Assert
        assertThrows(RuntimeException.class, () -> matchService.updateMatch(1L, updatedMatch));
        verify(entityCache).clear();
    }

    /**
     * Method under test: {@link MatchService#updateMatch(Long, Match)}
     */
    @Test
    void testUpdateMatch3() {
        // Arrange
        Optional<Match> emptyResult = Optional.empty();
        when(matchRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);
        doNothing().when(entityCache).clear();

        Category category = new Category();
        category.setCategoryId(1L);
        category.setMatches(new HashSet<>());
        category.setName("Name");

        Match updatedMatch = new Match();
        updatedMatch.setCategory(category);
        updatedMatch.setDuration(1);
        updatedMatch.setMatchId(1L);
        updatedMatch.setPlayers(new HashSet<>());

        // Act
        matchService.updateMatch(1L, updatedMatch);

        // Assert that nothing has changed
        verify(entityCache).clear();
        verify(matchRepository).findById(1L);
        assertEquals(1, updatedMatch.getDuration().intValue());
        assertEquals(1L, updatedMatch.getCategory().getCategoryId().longValue());
        assertEquals(1L, updatedMatch.getMatchId().longValue());
        assertTrue(updatedMatch.getPlayers().isEmpty());
    }

    /**
     * Method under test: {@link MatchService#addPlayer(Long, Long)}
     */
    @Test
    void testAddPlayer() {
        // Arrange
        Category category = new Category();
        category.setCategoryId(1L);
        category.setMatches(new HashSet<>());
        category.setName("Name");

        Match match = new Match();
        match.setCategory(category);
        match.setDuration(1);
        match.setMatchId(1L);
        match.setPlayers(new HashSet<>());
        Optional<Match> ofResult = Optional.of(match);

        Category category2 = new Category();
        category2.setCategoryId(1L);
        category2.setMatches(new HashSet<>());
        category2.setName("Name");

        Match match2 = new Match();
        match2.setCategory(category2);
        match2.setDuration(1);
        match2.setMatchId(1L);
        match2.setPlayers(new HashSet<>());
        when(matchRepository.save(Mockito.<Match>any())).thenReturn(match2);
        when(matchRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        Player player = new Player();
        player.setAccountId(1L);
        player.setMatches(new HashSet<>());
        player.setPersonalName("Personal Name");
        Optional<Player> ofResult2 = Optional.of(player);
        when(playerRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);

        // Act
        matchService.addPlayer(1L, 1L);

        // Assert
        verify(matchRepository).findById(1L);
        verify(playerRepository).findById(1L);
        verify(matchRepository).save(isA(Match.class));
    }

    /**
     * Method under test: {@link MatchService#addPlayer(Long, Long)}
     */
    @Test
    void testAddPlayer2() {
        // Arrange
        Category category = new Category();
        category.setCategoryId(1L);
        category.setMatches(new HashSet<>());
        category.setName("Name");

        Match match = new Match();
        match.setCategory(category);
        match.setDuration(1);
        match.setMatchId(1L);
        match.setPlayers(new HashSet<>());
        Optional<Match> ofResult = Optional.of(match);
        when(matchRepository.save(Mockito.<Match>any()))
                .thenThrow(new RuntimeException("player has been placed to matches"));
        when(matchRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        Player player = new Player();
        player.setAccountId(1L);
        player.setMatches(new HashSet<>());
        player.setPersonalName("Personal Name");
        Optional<Player> ofResult2 = Optional.of(player);
        when(playerRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);

        // Act and Assert
        assertThrows(RuntimeException.class, () -> matchService.addPlayer(1L, 1L));
        verify(matchRepository).findById(1L);
        verify(playerRepository).findById(1L);
        verify(matchRepository).save(isA(Match.class));
    }

    /**
     * Method under test: {@link MatchService#addPlayer(Long, Long)}
     */
    @Test
    void testAddPlayer3() {
        // Arrange
        Optional<Match> emptyResult = Optional.empty();
        when(matchRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);

        Player player = new Player();
        player.setAccountId(1L);
        player.setMatches(new HashSet<>());
        player.setPersonalName("Personal Name");
        Optional<Player> ofResult = Optional.of(player);
        when(playerRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        // Act and Assert
        assertThrows(RuntimeException.class, () -> matchService.addPlayer(1L, 1L));
        verify(matchRepository).findById(1L);
        verify(playerRepository).findById(1L);
    }

    /**
     * Method under test: {@link MatchService#removePlayerFromMatch(Long, Long)}
     */
    @Test
    void testRemovePlayerFromMatch() {
        // Arrange
        Category category = new Category();
        category.setCategoryId(1L);
        category.setMatches(new HashSet<>());
        category.setName("Name");

        Match match = new Match();
        match.setCategory(category);
        match.setDuration(1);
        match.setMatchId(1L);
        match.setPlayers(new HashSet<>());
        Optional<Match> ofResult = Optional.of(match);

        Category category2 = new Category();
        category2.setCategoryId(1L);
        category2.setMatches(new HashSet<>());
        category2.setName("Name");

        Match match2 = new Match();
        match2.setCategory(category2);
        match2.setDuration(1);
        match2.setMatchId(1L);
        match2.setPlayers(new HashSet<>());
        when(matchRepository.save(Mockito.<Match>any())).thenReturn(match2);
        when(matchRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        doNothing().when(entityCache).clear();

        // Act
        matchService.removePlayerFromMatch(1L, 1L);

        // Assert
        verify(entityCache).clear();
        verify(matchRepository).findById(1L);
        verify(matchRepository).save(isA(Match.class));
    }

    /**
     * Method under test: {@link MatchService#removePlayerFromMatch(Long, Long)}
     */
    @Test
    void testRemovePlayerFromMatch2() {
        // Arrange
        doThrow(new RuntimeException("player has been removed from matches")).when(entityCache).clear();

        // Act and Assert
        assertThrows(RuntimeException.class, () -> matchService.removePlayerFromMatch(1L, 1L));
        verify(entityCache).clear();
    }

    /**
     * Method under test: {@link MatchService#removePlayerFromMatch(Long, Long)}
     */
    @Test
    void testRemovePlayerFromMatch3() {
        // Arrange
        Category category = new Category();
        category.setCategoryId(1L);
        category.setMatches(new HashSet<>());
        category.setName("Name");

        Player player = new Player();
        player.setAccountId(1L);
        player.setMatches(new HashSet<>());
        player.setPersonalName("player has been removed from matches");

        HashSet<Player> players = new HashSet<>();
        players.add(player);

        Match match = new Match();
        match.setCategory(category);
        match.setDuration(1);
        match.setMatchId(1L);
        match.setPlayers(players);
        Optional<Match> ofResult = Optional.of(match);

        Category category2 = new Category();
        category2.setCategoryId(1L);
        category2.setMatches(new HashSet<>());
        category2.setName("Name");

        Match match2 = new Match();
        match2.setCategory(category2);
        match2.setDuration(1);
        match2.setMatchId(1L);
        match2.setPlayers(new HashSet<>());
        when(matchRepository.save(Mockito.<Match>any())).thenReturn(match2);
        when(matchRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        doNothing().when(entityCache).clear();

        // Act
        matchService.removePlayerFromMatch(1L, 1L);

        // Assert
        verify(entityCache).clear();
        verify(matchRepository).findById(1L);
        verify(matchRepository).save(isA(Match.class));
    }

    /**
     * Method under test: {@link MatchService#removePlayerFromMatch(Long, Long)}
     */
    @Test
    void testRemovePlayerFromMatch4() {
        // Arrange
        Category category = new Category();
        category.setCategoryId(1L);
        category.setMatches(new HashSet<>());
        category.setName("Name");

        Player player = new Player();
        player.setAccountId(1L);
        player.setMatches(new HashSet<>());
        player.setPersonalName("player has been removed from matches");

        Player player2 = new Player();
        player2.setAccountId(2L);
        player2.setMatches(new HashSet<>());
        player2.setPersonalName("Personal Name");

        HashSet<Player> players = new HashSet<>();
        players.add(player2);
        players.add(player);

        Match match = new Match();
        match.setCategory(category);
        match.setDuration(1);
        match.setMatchId(1L);
        match.setPlayers(players);
        Optional<Match> ofResult = Optional.of(match);

        Category category2 = new Category();
        category2.setCategoryId(1L);
        category2.setMatches(new HashSet<>());
        category2.setName("Name");

        Match match2 = new Match();
        match2.setCategory(category2);
        match2.setDuration(1);
        match2.setMatchId(1L);
        match2.setPlayers(new HashSet<>());
        when(matchRepository.save(Mockito.<Match>any())).thenReturn(match2);
        when(matchRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        doNothing().when(entityCache).clear();

        // Act
        matchService.removePlayerFromMatch(1L, 1L);

        // Assert
        verify(entityCache).clear();
        verify(matchRepository).findById(1L);
        verify(matchRepository).save(isA(Match.class));
    }

    /**
     * Method under test: {@link MatchService#removePlayerFromMatch(Long, Long)}
     */
    @Test
    void testRemovePlayerFromMatch5() {
        // Arrange
        Optional<Match> emptyResult = Optional.empty();
        when(matchRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);
        doNothing().when(entityCache).clear();

        // Act
        matchService.removePlayerFromMatch(1L, 1L);

        // Assert that nothing has changed
        verify(entityCache).clear();
        verify(matchRepository).findById(1L);
    }
}
