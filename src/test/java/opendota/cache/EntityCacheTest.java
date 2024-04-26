package opendota.cache;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class EntityCacheTest {

    @Test
    void putAndGet_ValidKeyAndValue_ShouldReturnInsertedValue() {
        // Arrange
        EntityCache<String, String> cache = new EntityCache<>();
        String key = "key";
        String value = "value";

        // Act
        cache.put(key, value);
        String retrievedValue = cache.get(key);

        // Assert
        assertEquals(value, retrievedValue);
    }

    @Test
    void remove_ValidKey_ShouldRemoveEntry() {
        // Arrange
        EntityCache<String, String> cache = new EntityCache<>();
        String key = "key";
        String value = "value";
        cache.put(key, value);

        // Act
        cache.remove(key);
        String retrievedValue = cache.get(key);

        // Assert
        assertNull(retrievedValue);
    }

    @Test
    void clear_CacheNotEmpty_ShouldClearCache() {
        // Arrange
        EntityCache<String, String> cache = new EntityCache<>();
        cache.put("key1", "value1");
        cache.put("key2", "value2");
        cache.put("key3", "value3");

        // Act
        cache.clear();

        // Assert
        assertEquals(0, cache.cacheMap.size());
    }

//    @Test
//    void put_MaxSizeExceeded_ShouldClearCache() {
//        // Arrange
//        EntityCache<Integer, String> cache = new EntityCache<>();
//        for (int i = 0; i < 110; i++) {
//            cache.put(i, "value");
//        }
//
//        // Act
//        cache.put(111, "new value");
//
//        // Assert
//        assertEquals(1, cache.cacheMap.size());
//    }
}
