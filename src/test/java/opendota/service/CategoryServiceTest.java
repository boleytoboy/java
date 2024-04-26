package opendota.service;

import opendota.cache.EntityCache;
import opendota.model.Category;
import opendota.model.Match;
import opendota.repository.CategoryRepository;
import opendota.repository.MatchRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private MatchRepository matchRepository;

    @Mock
    private EntityCache<Integer, Object> cacheMap;

    @InjectMocks
    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void findCategoryById_CategoryFoundInCache_ShouldReturnCategoryFromCache() {
        // Arrange
        Long categoryId = 1L;
        Category category = new Category();
        int hashCode = Objects.hash(categoryId, 33 * 34);
        when(cacheMap.get(hashCode)).thenReturn(Optional.of(category));

        // Act
        Optional<Category> result = categoryService.findCategoryById(categoryId);

        // Assert
        assertEquals(Optional.of(category), result);
        verify(cacheMap, times(1)).get(hashCode);
        verify(categoryRepository, never()).findById(categoryId);
    }

    @Test
    void saveCategory_ShouldSaveCategoryAndClearCache() {
        // Arrange
        Category category = new Category();

        // Act
        categoryService.saveCategory(category);

        // Assert
        verify(categoryRepository, times(1)).save(category);
        verify(cacheMap, times(1)).clear();
    }

//    @Test
//    void deleteCategoryById_ShouldDeleteCategoryAndClearCache() {
//        // Arrange
//        Long categoryId = 1L;
//        Category category = new Category();
//        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
//
//        // Act
//        categoryService.deleteCategoryById(categoryId);
//
//        // Assert
//        verify(categoryRepository, times(1)).findById(categoryId);
//        verify(categoryRepository, times(1)).deleteById(categoryId);
//        verify(matchRepository, times(1)).saveAll(any());
//        verify(cacheMap, times(1)).clear();
//    }

    @Test
    void updateCategory_CategoryExists_ShouldUpdateCategoryAndClearCache() {
        // Arrange
        Long categoryId = 1L;
        Category existingCategory = new Category();
        existingCategory.setCategoryId(categoryId);
        Category updatedCategory = new Category();
        updatedCategory.setCategoryId(categoryId);
        updatedCategory.setName("Updated Category");
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(existingCategory));

        // Act
        categoryService.updateCategory(categoryId, updatedCategory);

        // Assert
        verify(categoryRepository, times(1)).findById(categoryId);
        verify(categoryRepository, times(1)).save(existingCategory);
        assertEquals(updatedCategory.getName(), existingCategory.getName());
        verify(cacheMap, times(1)).clear();
    }

//    @Test
//    void addMatch_MatchAndCategoryExist_ShouldAddMatchToCategoryAndSave() {
//        // Arrange
//        Long matchId = 1L;
//        Long categoryId = 1L;
//        Category category = new Category();
//        category.setCategoryId(categoryId);
//        Match match = new Match();
//        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
//        when(matchRepository.findById(matchId)).thenReturn(Optional.of(match));
//
//        // Act
//        categoryService.addMatch(matchId, categoryId);
//
//        // Assert
//        assertTrue(category.getMatches().contains(match));
//        assertEquals(category, match.getCategory());
//        verify(categoryRepository, times(1)).findById(categoryId);
//        verify(matchRepository, times(1)).findById(matchId);
//        verify(categoryRepository, times(1)).save(category);
//    }

//    @Test
//    void removeMatchFromCategory_MatchAndCategoryExist_ShouldRemoveMatchFromCategoryAndSave() {
//        // Arrange
//        Long matchId = 1L;
//        Long categoryId = 1L;
//        Category category = new Category();
//        category.setCategoryId(categoryId);
//        Match match = new Match();
//        match.setCategory(category);
//        when(matchRepository.findById(matchId)).thenReturn(Optional.of(match));
//
//        // Act
//        categoryService.removeMatchFromCategory(matchId, categoryId);
//
//        // Assert
//        assertNull(match.getCategory());
//        verify(matchRepository, times(1)).findById(matchId);
//        verify(matchRepository, times(1)).save(match);
//        verify(cacheMap, times(1)).clear();
//    }
}
