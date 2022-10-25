package cache;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class LruCacheTestRegardlessExpirationTime {
    private static final int CAPACITY = 3;
    private static final int EXPIRE_TIME = 1;
    private static final TimeUnit TIME_UNIT = TimeUnit.HOURS;

    private LruCache<String, String> lruCacheUnderTest;

    @BeforeEach
    void setup() {
        lruCacheUnderTest = new LruCache<>(CAPACITY, EXPIRE_TIME, TIME_UNIT);
    }

    /* BASIC CACHE OPERATION TEST TO REGARDLESS EXPIRE TIME */

    @Test

    @DisplayName("addSomeDataToCache_WhenGetData_ThenIsEqualWithCacheElement")
    void addSomeDataToCache_WhenGetData_ThenIsEqualWithCacheElement() {
        lruCacheUnderTest.put("1", "test1");
        lruCacheUnderTest.put("2", "test2");
        lruCacheUnderTest.put("3", "test3");
        assertEquals("test1", lruCacheUnderTest.get("1"), "requesting from cache key '1' should respond with 'test1'");
        assertEquals("test2", lruCacheUnderTest.get("2"), "requesting from cache key '2' should respond with 'test3'");
        assertEquals("test3", lruCacheUnderTest.get("3"), "requesting from cache key '3' should respond with 'test3'");
    }

    @Test
    @DisplayName("addSomeDataToCache_ChaneTheValueForSameKey_ThenIsEqualWithCacheElement")
    void addSomeDataToCache_ChaneTheValueForSameKey_ThenIsEqualWithCacheElement() {
        lruCacheUnderTest.put("1", "test1");
        lruCacheUnderTest.put("1", "testChangedValueForSameKey");
        assertEquals("testChangedValueForSameKey", lruCacheUnderTest.get("1"), "requesting from cache key '1' should respond with 'testChangedValueForSameKey'");
    }


    @Test
    @DisplayName("addDataToCacheToTheNumberOfSize_WhenAddOneMoreData_ThenLeastRecentlyDataWillEvict")
    void addDataToCacheToTheNumberOfSize_WhenAddOneMoreData_ThenLeastRecentlyDataWillEvict() {
        lruCacheUnderTest.put("1", "test1");
        lruCacheUnderTest.put("2", "test2");
        lruCacheUnderTest.put("3", "test3");
        lruCacheUnderTest.put("4", "test4");
        assertNull(lruCacheUnderTest.get("1"), "requesting from cache key '1' after inserting 3 other keys, should be null.");
    }

    @Test
    void addBunchOfDataToCacheExceedsTheNumberOfSize_WhenGetData_ThenIsEqualWithCacheElement() {
        final int BUNCH_SIZE = CAPACITY * 1000;
        for (int i = 0; i < BUNCH_SIZE; i++) {
            lruCacheUnderTest.put(String.valueOf(i), "test" + i);
        }
        IntStream.range(CAPACITY * 1000 - CAPACITY, CAPACITY).forEach(i -> assertEquals("test" + i, lruCacheUnderTest.get(String.valueOf(i)), "requesting from cache key '" + i + "' should respond with 'test" + i + "'"));
    }

    @Test
    void getDataFromCache_WhenCacheIsEmpty_ShouldReturnNull() {
        assertNull(lruCacheUnderTest.get("1"), "requesting from cache key '1' when cache is empty, should be null.");
        assertNull(lruCacheUnderTest.get("2"), "requesting from cache key '2' when cache is empty, should be null.");
    }

    @Test
    void deleteDataFromCache_WhenCacheIsEmpty_ShouldDoNone() {
        lruCacheUnderTest.delete("2");
        assertNull(lruCacheUnderTest.get("2"), "requesting from cache key '2' when cache is empty, should be null.");
        lruCacheUnderTest.put("2", "test2");
        assertEquals("test2", lruCacheUnderTest.get("2"), "requesting from cache key '2' should respond with 'test3'");
        lruCacheUnderTest.delete("2");
        assertNull(lruCacheUnderTest.get("2"), "requesting from cache key '2' when cache is empty, should be null.");

    }


    @Test
    @DisplayName("addSomeDataToCache_ChaneTheValueForSameKey_ThenIsEqualWithCacheElement")
    void addSomeDataToCache_ThenGetThisData_ThenLeastRecentlyDataWillEvict() {
        lruCacheUnderTest.put("1", "test1");
        lruCacheUnderTest.put("2", "test2");
        lruCacheUnderTest.put("3", "test3");
        assertEquals("test1", lruCacheUnderTest.get("1"), "requesting from cache key '1' should respond with 'test1'");
        lruCacheUnderTest.put("4", "test4");
        assertEquals("test1", lruCacheUnderTest.get("1"), "requesting from cache key '1' should respond with 'test1'");
        assertNull(lruCacheUnderTest.get("2"), "requesting from cache key '2' after inserting 3 other keys, should be null.");
        assertEquals("test3", lruCacheUnderTest.get("3"), "requesting from cache key '3' should respond with 'test3'");
        assertEquals("test4", lruCacheUnderTest.get("4"), "requesting from cache key '4' should respond with 'test4'");
    }

    @Test
    void addDataToCacheToTheNumberOfSize_DeleteOne_ThenValidDataIsEqualWithCacheElement() {
        lruCacheUnderTest.put("1", "test1");
        lruCacheUnderTest.put("2", "test2");
        lruCacheUnderTest.put("3", "test3");
        lruCacheUnderTest.delete("2");
        assertEquals("test1", lruCacheUnderTest.get("1"), "requesting from cache key '1' should respond with 'test1'");
        assertNull(lruCacheUnderTest.get("2"), "requesting from cache key '2' after inserting deleting it, should be null.");
        assertEquals("test3", lruCacheUnderTest.get("3"), "requesting from cache key '3' should respond with 'test3'");
    }

    @Test
    void addDataToCacheToTheNumberOfSize_DeleteOne_ThenInsertAgain_ThenValidDataIsEqualWithCacheElement() {
        lruCacheUnderTest.put("1", "test1");
        lruCacheUnderTest.put("2", "test2");
        lruCacheUnderTest.put("3", "test3");
        lruCacheUnderTest.delete("2");
        lruCacheUnderTest.put("4", "test4");
        assertEquals("test1", lruCacheUnderTest.get("1"), "requesting from cache key '1' should respond with 'test1'");
        assertNull(lruCacheUnderTest.get("2"), "requesting from cache key '2' after inserting deleting it, should be null.");
        assertEquals("test3", lruCacheUnderTest.get("3"), "requesting from cache key '3' should respond with 'test3'");
        assertEquals("test4", lruCacheUnderTest.get("4"), "requesting from cache key '4' should respond with 'test4'");
    }

    @Test
    void addSameDataToCacheToTheNumberOfSize_AndThenToAddOtherData_WhenGetData_ThenIsEqualWithCacheElement() {
        lruCacheUnderTest.put("1", "test1");
        lruCacheUnderTest.put("1", "test1");
        lruCacheUnderTest.put("1", "test1");
        lruCacheUnderTest.put("2", "test2");
        lruCacheUnderTest.put("3", "test3");
        lruCacheUnderTest.put("4", "test4");
        assertNull(lruCacheUnderTest.get("1"), "requesting from cache key '1' after inserting 3 other keys, should be null.");
        assertEquals("test2", lruCacheUnderTest.get("2"), "requesting from cache key '2' should respond with 'test2'");
        assertEquals("test3", lruCacheUnderTest.get("3"), "requesting from cache key '3' should respond with 'test3'");
        assertEquals("test4", lruCacheUnderTest.get("4"), "requesting from cache key '4' should respond with 'test4'");
    }


}