package cache;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.fail;

public class LruCacheTestInvalidInputs {

    private static final int CAPACITY = 3;
    private static final int EXPIRE_TIME = 1;
    private static final TimeUnit TIME_UNIT = TimeUnit.HOURS;

    private LruCache<String, String> lruCacheUnderTest;


    @Test
    public void constructorDoesNotAllowZeroCacheSize() {
        try {
            new LruCache<String, String>(0, EXPIRE_TIME, TIME_UNIT);
            fail();
        } catch (IllegalArgumentException expected) {
            //all is ok
        } catch (Exception e) {
            fail("We are expecting to fail with IllegalArgumentException on that one, but from some reason we fail with: " + e.getMessage());
        }
    }

    @Test
    public void cannotPutNullKey() {
        try {
            lruCacheUnderTest = new LruCache<String, String>(CAPACITY, EXPIRE_TIME, TIME_UNIT);

            lruCacheUnderTest.put(null, "a");
            fail();
        } catch (NullPointerException expected) {
            //all is ok
        } catch (Exception e) {
            fail("We are expecting to fail with NullPointerException on that one, but from some reason we fail with: " + e.getMessage());
        }
    }

    @Test
    public void cannotPutNullValue() {
        try {
            lruCacheUnderTest = new LruCache<String, String>(CAPACITY, EXPIRE_TIME, TIME_UNIT);
            lruCacheUnderTest.put("a", null);
            fail();
        } catch (NullPointerException expected) {
            //all is ok
        } catch (Exception e) {
            fail("We are expecting to fail with NullPointerException on that one, but from some reason we fail with: " + e.getMessage());
        }
    }

    @Test
    public void cannotRemoveNullKey() {
        try {
            lruCacheUnderTest = new LruCache<String, String>(CAPACITY, EXPIRE_TIME, TIME_UNIT);
            lruCacheUnderTest.delete(null);
            fail();
        } catch (NullPointerException expected) {
            //all is ok
        } catch (Exception e) {
            fail("We are expecting to fail with NullPointerException on that one, but from some reason we fail with: " + e.getMessage());
        }
    }

    @Test
    public void throwsWithNullKey() {
        try {
            lruCacheUnderTest = new LruCache<String, String>(CAPACITY, EXPIRE_TIME, TIME_UNIT);
            lruCacheUnderTest.get(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // nothing
        }
    }
}
