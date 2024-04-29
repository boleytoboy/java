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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import opendota.cache.EntityCache;
import opendota.model.Category;
import opendota.model.Match;
import opendota.repository.CategoryRepository;
import opendota.repository.MatchRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {CategoryService.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class CategoryServiceDiffblueTest {
    @MockBean
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryService categoryService;

    @MockBean
    private EntityCache<Integer, Object> entityCache;

    @MockBean
    private MatchRepository matchRepository;

    /**
     * Method under test: {@link CategoryService#findCategoryById(Long)}
     */
    @Test
    void testFindCategoryById() {
        // Arrange
        when(entityCache.get(Mockito.<Integer>any()))
                .thenThrow(new RuntimeException("category info is taken from DB and placed into the cache"));

        // Act and Assert
        assertThrows(RuntimeException.class, () -> categoryService.findCategoryById(1L));
        verify(entityCache).get(eq(2114));
    }

    /**
     * Method under test: {@link CategoryService#findCategoryById(Long)}
     */
    @Test
    void testFindCategoryById2() {
        // Arrange
        Category category = mock(Category.class);
        doNothing().when(category).setCategoryId(Mockito.<Long>any());
        doNothing().when(category).setMatches(Mockito.<Set<Match>>any());
        doNothing().when(category).setName(Mockito.<String>any());
        category.setCategoryId(1L);
        category.setMatches(new HashSet<>());
        category.setName("Name");
        Optional.of(category);
        Optional<Object> ofResult = Optional.of("42");
        when(entityCache.get(Mockito.<Integer>any())).thenReturn(ofResult);

        // Act
        Optional<Category> actualFindCategoryByIdResult = categoryService.findCategoryById(1L);

        // Assert
        verify(entityCache).get(eq(2114));
        verify(category).setCategoryId(eq(1L));
        verify(category).setMatches(isA(Set.class));
        verify(category).setName(eq("Name"));
        assertSame(ofResult, actualFindCategoryByIdResult);
    }

    /**
     * Method under test: {@link CategoryService#findCategoryById(Long)}
     */
    @Test
    void testFindCategoryById3() {
        // Arrange
        Category category = mock(Category.class);
        doNothing().when(category).setCategoryId(Mockito.<Long>any());
        doNothing().when(category).setMatches(Mockito.<Set<Match>>any());
        doNothing().when(category).setName(Mockito.<String>any());
        category.setCategoryId(1L);
        category.setMatches(new HashSet<>());
        category.setName("Name");
        Optional<Category> ofResult = Optional.of(category);
        when(categoryRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        when(entityCache.get(Mockito.<Integer>any())).thenReturn(null);
        doNothing().when(entityCache).put(Mockito.<Integer>any(), Mockito.<Object>any());

        // Act
        Optional<Category> actualFindCategoryByIdResult = categoryService.findCategoryById(1L);

        // Assert
        verify(entityCache).get(eq(2114));
        verify(entityCache).put(eq(2114), isA(Object.class));
        verify(category).setCategoryId(eq(1L));
        verify(category).setMatches(isA(Set.class));
        verify(category).setName(eq("Name"));
        verify(categoryRepository).findById(eq(1L));
        assertSame(ofResult, actualFindCategoryByIdResult);
    }

    /**
     * Method under test: {@link CategoryService#saveCategory(Category)}
     */
    @Test
    void testSaveCategory() {
        // Arrange
        Category category = new Category();
        category.setCategoryId(1L);
        category.setMatches(new HashSet<>());
        category.setName("Name");
        when(categoryRepository.save(Mockito.<Category>any())).thenReturn(category);
        doNothing().when(entityCache).clear();

        Category category2 = new Category();
        category2.setCategoryId(1L);
        category2.setMatches(new HashSet<>());
        category2.setName("Name");

        // Act
        Category actualSaveCategoryResult = categoryService.saveCategory(category2);

        // Assert
        verify(entityCache).clear();
        verify(categoryRepository).save(isA(Category.class));
        assertSame(category, actualSaveCategoryResult);
    }

    /**
     * Method under test: {@link CategoryService#saveCategory(Category)}
     */
    @Test
    void testSaveCategory2() {
        // Arrange
        doThrow(new RuntimeException("category info has been saved")).when(entityCache).clear();

        Category category = new Category();
        category.setCategoryId(1L);
        category.setMatches(new HashSet<>());
        category.setName("Name");

        // Act and Assert
        assertThrows(RuntimeException.class, () -> categoryService.saveCategory(category));
        verify(entityCache).clear();
    }

    /**
     * Method under test: {@link CategoryService#deleteCategoryById(Long)}
     */
    @Test
    void testDeleteCategoryById() {
        // Arrange
        Category category = new Category();
        category.setCategoryId(1L);
        category.setMatches(new HashSet<>());
        category.setName("Name");
        Optional<Category> ofResult = Optional.of(category);
        doNothing().when(categoryRepository).deleteById(Mockito.<Long>any());
        when(categoryRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        when(matchRepository.saveAll(Mockito.<Iterable<Match>>any())).thenReturn(new ArrayList<>());
        doNothing().when(entityCache).clear();

        // Act
        categoryService.deleteCategoryById(1L);

        // Assert that nothing has changed
        verify(entityCache).clear();
        verify(categoryRepository).deleteById(eq(1L));
        verify(categoryRepository).findById(eq(1L));
        verify(matchRepository).saveAll(isA(Iterable.class));
    }

    /**
     * Method under test: {@link CategoryService#deleteCategoryById(Long)}
     */
    @Test
    void testDeleteCategoryById2() {
        // Arrange
        doThrow(new RuntimeException("information in the database has been deleted")).when(entityCache).clear();

        // Act and Assert
        assertThrows(RuntimeException.class, () -> categoryService.deleteCategoryById(1L));
        verify(entityCache).clear();
    }

    /**
     * Method under test: {@link CategoryService#deleteCategoryById(Long)}
     */
    @Test
    void testDeleteCategoryById3() {
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

        Category category2 = new Category();
        category2.setCategoryId(1L);
        category2.setMatches(matches);
        category2.setName("Name");
        Optional<Category> ofResult = Optional.of(category2);
        doNothing().when(categoryRepository).deleteById(Mockito.<Long>any());
        when(categoryRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        when(matchRepository.saveAll(Mockito.<Iterable<Match>>any())).thenReturn(new ArrayList<>());
        doNothing().when(entityCache).clear();

        // Act
        categoryService.deleteCategoryById(1L);

        // Assert
        verify(entityCache).clear();
        verify(categoryRepository).deleteById(eq(1L));
        verify(categoryRepository).findById(eq(1L));
        verify(matchRepository).saveAll(isA(Iterable.class));
    }

    /**
     * Method under test: {@link CategoryService#updateCategory(Long, Category)}
     */
    @Test
    void testUpdateCategory() {
        // Arrange
        Category category = new Category();
        category.setCategoryId(1L);
        category.setMatches(new HashSet<>());
        category.setName("Name");
        Optional<Category> ofResult = Optional.of(category);

        Category category2 = new Category();
        category2.setCategoryId(1L);
        category2.setMatches(new HashSet<>());
        category2.setName("Name");
        when(categoryRepository.save(Mockito.<Category>any())).thenReturn(category2);
        when(categoryRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        doNothing().when(entityCache).clear();

        Category updatedCategory = new Category();
        updatedCategory.setCategoryId(1L);
        updatedCategory.setMatches(new HashSet<>());
        updatedCategory.setName("Name");

        // Act
        categoryService.updateCategory(1L, updatedCategory);

        // Assert
        verify(entityCache).clear();
        verify(categoryRepository).findById(eq(1L));
        verify(categoryRepository).save(isA(Category.class));
    }

    /**
     * Method under test: {@link CategoryService#updateCategory(Long, Category)}
     */
    @Test
    void testUpdateCategory2() {
        // Arrange
        doThrow(new RuntimeException("information in the database has been updated")).when(entityCache).clear();

        Category updatedCategory = new Category();
        updatedCategory.setCategoryId(1L);
        updatedCategory.setMatches(new HashSet<>());
        updatedCategory.setName("Name");

        // Act and Assert
        assertThrows(RuntimeException.class, () -> categoryService.updateCategory(1L, updatedCategory));
        verify(entityCache).clear();
    }

    /**
     * Method under test: {@link CategoryService#updateCategory(Long, Category)}
     */
    @Test
    void testUpdateCategory3() {
        // Arrange
        Optional<Category> emptyResult = Optional.empty();
        when(categoryRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);
        doNothing().when(entityCache).clear();

        Category updatedCategory = new Category();
        updatedCategory.setCategoryId(1L);
        updatedCategory.setMatches(new HashSet<>());
        updatedCategory.setName("Name");

        // Act
        categoryService.updateCategory(1L, updatedCategory);

        // Assert that nothing has changed
        verify(entityCache).clear();
        verify(categoryRepository).findById(eq(1L));
        assertEquals("Name", updatedCategory.getName());
        assertEquals(1L, updatedCategory.getCategoryId().longValue());
        assertTrue(updatedCategory.getMatches().isEmpty());
    }

    /**
     * Method under test: {@link CategoryService#addMatch(Long, Long)}
     */
    @Test
    void testAddMatch() {
        // Arrange
        Category category = new Category();
        category.setCategoryId(1L);
        category.setMatches(new HashSet<>());
        category.setName("Name");
        Optional<Category> ofResult = Optional.of(category);

        Category category2 = new Category();
        category2.setCategoryId(1L);
        category2.setMatches(new HashSet<>());
        category2.setName("Name");
        when(categoryRepository.save(Mockito.<Category>any())).thenReturn(category2);
        when(categoryRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        Category category3 = new Category();
        category3.setCategoryId(1L);
        category3.setMatches(new HashSet<>());
        category3.setName("Name");

        Match match = new Match();
        match.setCategory(category3);
        match.setDuration(1);
        match.setMatchId(1L);
        match.setPlayers(new HashSet<>());
        Optional<Match> ofResult2 = Optional.of(match);
        when(matchRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);

        // Act
        categoryService.addMatch(1L, 1L);

        // Assert
        verify(categoryRepository).findById(eq(1L));
        verify(matchRepository).findById(eq(1L));
        verify(categoryRepository).save(isA(Category.class));
    }

    /**
     * Method under test: {@link CategoryService#addMatch(Long, Long)}
     */
    @Test
    void testAddMatch2() {
        // Arrange
        Category category = new Category();
        category.setCategoryId(1L);
        category.setMatches(new HashSet<>());
        category.setName("Name");
        Optional<Category> ofResult = Optional.of(category);
        when(categoryRepository.save(Mockito.<Category>any()))
                .thenThrow(new RuntimeException("match has been placed to category"));
        when(categoryRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        Category category2 = new Category();
        category2.setCategoryId(1L);
        category2.setMatches(new HashSet<>());
        category2.setName("Name");

        Match match = new Match();
        match.setCategory(category2);
        match.setDuration(1);
        match.setMatchId(1L);
        match.setPlayers(new HashSet<>());
        Optional<Match> ofResult2 = Optional.of(match);
        when(matchRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);

        // Act and Assert
        assertThrows(RuntimeException.class, () -> categoryService.addMatch(1L, 1L));
        verify(categoryRepository).findById(eq(1L));
        verify(matchRepository).findById(eq(1L));
        verify(categoryRepository).save(isA(Category.class));
    }

    /**
     * Method under test: {@link CategoryService#addMatch(Long, Long)}
     */
    @Test
    void testAddMatch3() {
        // Arrange
        Category category = new Category();
        category.setCategoryId(1L);
        category.setMatches(new HashSet<>());
        category.setName("Name");
        Optional<Category> ofResult = Optional.of(category);
        when(categoryRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        Optional<Match> emptyResult = Optional.empty();
        when(matchRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(RuntimeException.class, () -> categoryService.addMatch(1L, 1L));
        verify(categoryRepository).findById(eq(1L));
        verify(matchRepository).findById(eq(1L));
    }

    /**
     * Method under test:
     * {@link CategoryService#removeMatchFromCategory(Long, Long)}
     */
    @Test
    void testRemoveMatchFromCategory() {
        // Arrange
        Category category = new Category();
        category.setCategoryId(1L);
        category.setMatches(new HashSet<>());
        category.setName("Name");
        Optional<Category> ofResult = Optional.of(category);
        when(categoryRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        Category category2 = new Category();
        category2.setCategoryId(1L);
        category2.setMatches(new HashSet<>());
        category2.setName("Name");

        Match match = new Match();
        match.setCategory(category2);
        match.setDuration(1);
        match.setMatchId(1L);
        match.setPlayers(new HashSet<>());
        Optional<Match> ofResult2 = Optional.of(match);

        Category category3 = new Category();
        category3.setCategoryId(1L);
        category3.setMatches(new HashSet<>());
        category3.setName("Name");

        Match match2 = new Match();
        match2.setCategory(category3);
        match2.setDuration(1);
        match2.setMatchId(1L);
        match2.setPlayers(new HashSet<>());
        when(matchRepository.save(Mockito.<Match>any())).thenReturn(match2);
        when(matchRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);
        doNothing().when(entityCache).clear();

        // Act
        categoryService.removeMatchFromCategory(1L, 1L);

        // Assert
        verify(entityCache).clear();
        verify(categoryRepository).findById(eq(1L));
        verify(matchRepository).findById(eq(1L));
        verify(matchRepository).save(isA(Match.class));
    }

    /**
     * Method under test:
     * {@link CategoryService#removeMatchFromCategory(Long, Long)}
     */
    @Test
    void testRemoveMatchFromCategory2() {
        // Arrange
        doThrow(new RuntimeException("match has been removed from category")).when(entityCache).clear();

        // Act and Assert
        assertThrows(RuntimeException.class, () -> categoryService.removeMatchFromCategory(1L, 1L));
        verify(entityCache).clear();
    }

    /**
     * Method under test:
     * {@link CategoryService#removeMatchFromCategory(Long, Long)}
     */
    @Test
    void testRemoveMatchFromCategory3() {
        // Arrange
        Optional<Category> emptyResult = Optional.empty();
        when(categoryRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);

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
        when(matchRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        doNothing().when(entityCache).clear();

        // Act
        categoryService.removeMatchFromCategory(1L, 1L);

        // Assert that nothing has changed
        verify(entityCache).clear();
        verify(categoryRepository).findById(eq(1L));
    }
}
