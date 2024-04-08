package opendota.service;

import opendota.cache.EntityCache;
import opendota.model.Category;
import opendota.model.Match;
import opendota.repository.CategoryRepository;
import opendota.repository.MatchRepository;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final MatchRepository matchRepository;
    private final EntityCache<Integer,Object> cacheMap;
    public CategoryService(CategoryRepository categoryRepository, MatchRepository matchRepository, EntityCache<Integer, Object> cacheMap) {
        this.categoryRepository = categoryRepository;
        this.matchRepository = matchRepository;
        this.cacheMap = cacheMap;
    }
    public Optional<Category> findCategoryById(Long categoryId) {
        int hashCode = Objects.hash(categoryId, 33 * 34);
        Object cachedData = cacheMap.get(hashCode);

        if (cachedData != null) {
            return (Optional<Category>) cachedData;
        } else {
            Optional<Category> category = categoryRepository.findById(categoryId);
            cacheMap.put(hashCode, category);
            return category;
        }
    }
    public Category saveCategory(Category category){
        cacheMap.clear();
        return categoryRepository.save(category);
    }
    public void deleteCategoryById(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow();
        for (Match match : category.getMatches()) {
            match.setCategory(null);
        }
        matchRepository.saveAll(category.getMatches());
        categoryRepository.deleteById(categoryId);
    }
    public void updateCategory(Long categoryId, Category updatedCategory) {
        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
        if (categoryOptional.isPresent()) {
            Category category = categoryOptional.get();
            category.setName(updatedCategory.getName());
            categoryRepository.save(category);
        }
    }
    public void addMatch(Long matchId, Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow();
        Match match = matchRepository.findById(matchId).orElseThrow(() -> new RuntimeException("match not found"));
        category.addMatch(match);
        match.setCategory(category);
        categoryRepository.save(category);
    }
    public void removeMatchFromCategory(Long matchId, Long categoryId) {
        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
        if (categoryOptional.isPresent()) {
            Match match = matchRepository.findById(matchId).orElseThrow();
            match.setCategory(null);
            matchRepository.save(match);
        }
    }
}
