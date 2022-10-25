package cache;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class LRUCacheTestWithTimeExpiration {

    private static final int CAPACITY = 3;
    private static final int EXPIRE_TIME = 1;
    private static final TimeUnit TIME_UNIT = TimeUnit.MINUTES;
    private static int sleepTime;
    private LruCache<String, String> lruCacheUnderTest;

    @BeforeEach
    void setup() {
        lruCacheUnderTest = new LruCache<>(CAPACITY, EXPIRE_TIME, TIME_UNIT);

        switch (TIME_UNIT) {
            case SECONDS:
                sleepTime = EXPIRE_TIME * 1000;
                break;
            case MINUTES:
                sleepTime = EXPIRE_TIME * 60000;
                break;
            case HOURS:
                sleepTime = EXPIRE_TIME * 3600000;
                break;
            default://MILLISECONDS
                sleepTime = EXPIRE_TIME;
                break;
        }
    }
    /* BASIC CACHE OPERATION TESTs WITH EXPIRE TIME */

    @Test
    @DisplayName("addSomeDataToCache_WhenGetData_ThenIsEqualWithCacheElement")
    void addSomeDataToCache_WaitForExpirationTime_WhenGetData_ThenShouldReturnNull() throws InterruptedException {
        lruCacheUnderTest.put("1", "test1");
        lruCacheUnderTest.put("2", "test2");
        lruCacheUnderTest.put("3", "test3");

        System.out.println("TESTING: SLEEPING FOR " + EXPIRE_TIME * CAPACITY + " " + TIME_UNIT);
        Thread.sleep((long) sleepTime * CAPACITY);

        assertNull(lruCacheUnderTest.get("1"),
                "requesting from cache key '1' after waiting ExpirationTime , should be null.");

        assertNull(lruCacheUnderTest.get("2"),
                "requesting from cache key '2' after waiting ExpirationTime, should be null.");

        assertNull(lruCacheUnderTest.get("3"),
                "requesting from cache key '3' after waiting ExpirationTime, should be null.");
    }


    @Test
    void addSomeDataToCache_WaitForExpirationTime_AddMoreData_ThenEqualWithValidCacheElement() throws InterruptedException {
        lruCacheUnderTest.put("1", "test1");
        lruCacheUnderTest.put("2", "test2");

        assertEquals("test1", lruCacheUnderTest.get("1"),
                "requesting from cache key '1' should respond with 'test1'");

        assertEquals("test2", lruCacheUnderTest.get("2"),
                "requesting from cache key '2' should respond with 'test2'");

        System.out.println("TESTING: SLEEPING FOR " + EXPIRE_TIME * CAPACITY + " " + TIME_UNIT);
        Thread.sleep((long) sleepTime * CAPACITY);

        assertNull(lruCacheUnderTest.get("1"),
                "requesting from cache key '1' after waiting ExpirationTime , should be null.");

        assertNull(lruCacheUnderTest.get("2"),
                "requesting from cache key '2' after waiting ExpirationTime , should be null.");

        lruCacheUnderTest.put("3", "test3");
        lruCacheUnderTest.put("4", "test4");

        assertEquals("test3", lruCacheUnderTest.get("3"),
                "requesting from cache key '3' should respond with 'test3'");

        assertEquals("test4", lruCacheUnderTest.get("4"),
                "requesting from cache key '4' should respond with 'test4'");
    }


}
