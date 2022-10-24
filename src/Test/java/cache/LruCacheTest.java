package cache;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class LruCacheTest {
    private static final int CAPACITY = 3;
    private static final int EXPIRE_TIME = 1;
    private static final TimeUnit TIME_UNIT = TimeUnit.MINUTES;

    private LruCache<String, String> lruCacheUnderTest;

    @BeforeEach
    void setup() {
        lruCacheUnderTest = new LruCache<>(CAPACITY, EXPIRE_TIME, TIME_UNIT);
    }

    @Test
    @DisplayName("addSomeDataToCache_WhenGetData_ThenIsEqualWithCacheElement")
    void addSomeDataToCache_WhenGetData_ThenIsEqualWithCacheElement() {
        lruCacheUnderTest.put("1", "test1");
        lruCacheUnderTest.put("2", "test2");
        lruCacheUnderTest.put("3", "test3");
        assertEquals("test1", lruCacheUnderTest.get("1"), "requesting from cache key '1' should respond with 'test1'");
        assertEquals("test2", lruCacheUnderTest.get("2"), "requesting from cache key '2' should respond with 'test3'");
        assertEquals("test3", lruCacheUnderTest.get("3"), "requesting from cache key '3' should respond with 'test2'");
    }

    @Test

    @DisplayName("addDataToCacheToTheNumberOfSize_WhenAddOneMoreData_ThenLeastRecentlyDataWillEvict")
    void addDataToCacheToTheNumberOfSize_WhenAddOneMoreData_ThenLeastRecentlyDataWillEvict() throws InterruptedException {
        lruCacheUnderTest.put("1", "test1");
        lruCacheUnderTest.put("2", "test2");
        lruCacheUnderTest.put("3", "test3");
        lruCacheUnderTest.put("4", "test4");
        Thread.sleep(10000);
        assertNull(lruCacheUnderTest.get("1"));
    }

    @Test
    void delete() {
    }
}