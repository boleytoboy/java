package opendota.controller;

import opendota.model.Category;
import opendota.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/category")
public class CategoryController {
    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
        Category savedCategory = categoryService.saveCategory(category);
        return new ResponseEntity<>(savedCategory, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategory(@PathVariable("id") Long categoryId) {
        return categoryService.findCategoryById(categoryId)
                .map(category -> ResponseEntity.ok().body(category))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable("id") Long categoryId) {
        categoryService.deleteCategoryById(categoryId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCategory(@PathVariable("id") Long categoryId, @RequestBody Category category) {
        categoryService.updateCategory(categoryId, category);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{categoryId}/matches/{matchId}")
    public ResponseEntity<Void> addMatch(@PathVariable Long matchId, @PathVariable Long categoryId) {
        categoryService.addMatch(matchId, categoryId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{categoryId}/matches/{matchId}")
    public ResponseEntity<Void> removeMatchFromCategory(@PathVariable Long matchId, @PathVariable Long categoryId) {
        categoryService.removeMatchFromCategory(matchId, categoryId);
        return ResponseEntity.ok().build();
    }
}
