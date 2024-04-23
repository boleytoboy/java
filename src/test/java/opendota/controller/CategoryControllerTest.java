package opendota.controller;

import opendota.model.Category;
import opendota.service.CategoryService;
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

class CategoryControllerTest {

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void createCategory_ValidCategory_ShouldReturnCreatedResponse() {
        // Arrange
        Category category = new Category();
        when(categoryService.saveCategory(any())).thenReturn(category);

        // Act
        ResponseEntity<Category> response = categoryController.createCategory(category);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(category, response.getBody());
    }

    @Test
    void getCategory_ExistingCategoryId_ShouldReturnCategory() {
        // Arrange
        Long categoryId = 1L;
        Category category = new Category();
        when(categoryService.findCategoryById(categoryId)).thenReturn(Optional.of(category));

        // Act
        ResponseEntity<Category> response = categoryController.getCategory(categoryId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(category, response.getBody());
    }

    @Test
    void deleteCategory_ExistingCategoryId_ShouldReturnOkResponse() {
        // Arrange
        Long categoryId = 1L;

        // Act
        ResponseEntity<Void> response = categoryController.deleteCategory(categoryId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(categoryService, times(1)).deleteCategoryById(categoryId);
    }

    @Test
    void updateCategory_ExistingCategoryId_ShouldReturnOkResponse() {
        // Arrange
        Long categoryId = 1L;
        Category category = new Category();

        // Act
        ResponseEntity<Void> response = categoryController.updateCategory(categoryId, category);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(categoryService, times(1)).updateCategory(categoryId, category);
    }

    @Test
    void addMatch_ValidMatchAndCategoryId_ShouldReturnOkResponse() {
        // Arrange
        Long matchId = 1L;
        Long categoryId = 1L;

        // Act
        ResponseEntity<Void> response = categoryController.addMatch(matchId, categoryId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(categoryService, times(1)).addMatch(matchId, categoryId);
    }

    @Test
    void removeMatchFromCategory_ValidMatchAndCategoryId_ShouldReturnOkResponse() {
        // Arrange
        Long matchId = 1L;
        Long categoryId = 1L;

        // Act
        ResponseEntity<Void> response = categoryController.removeMatchFromCategory(matchId, categoryId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(categoryService, times(1)).removeMatchFromCategory(matchId, categoryId);
    }
}
