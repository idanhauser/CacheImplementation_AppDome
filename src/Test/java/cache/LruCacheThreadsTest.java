package cache;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LruCacheThreadsTest {
    private static final int CAPACITY = 100;
    private static final int EXPIRE_TIME = 1;
    private static final TimeUnit TIME_UNIT = TimeUnit.HOURS;

    private LruCache<String, String> lruCacheUnderTest;

    @BeforeEach
    void setup() {
        lruCacheUnderTest = new LruCache<>(CAPACITY, EXPIRE_TIME, TIME_UNIT);
    }

    @Test
    public void runMultiThreadTask_WhenPutDataInConcurrentToCache_ThenNoDataLost() throws Exception {
        final ExecutorService executorService = Executors.newFixedThreadPool(5);

        CountDownLatch countDownLatch = new CountDownLatch(CAPACITY);
        try {
            IntStream.range(0, CAPACITY).<Runnable>mapToObj(key -> () -> {
                lruCacheUnderTest.put(String.valueOf(key), "value" + String.valueOf(key));
                countDownLatch.countDown();
            }).forEach(executorService::submit);
            countDownLatch.await();
        } finally {
            executorService.shutdown();
        }

        IntStream.range(0, CAPACITY).forEach(i -> assertEquals("value" + i, lruCacheUnderTest.get(String.valueOf(i))));
    }
}
