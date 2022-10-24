import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        LruCache<String, Integer> lru = new LruCache<String, Integer>(3, 5, TimeUnit.SECONDS);

        lru.put(String.valueOf(1),  2);
        lru.put(String.valueOf(2), 2 * 2);
        lru.put(String.valueOf(3), 3 * 2);
        Thread.sleep(5000);
        Thread.sleep(5000);
        Thread.sleep(5000);
        Thread.sleep(5000);
        Thread.sleep(5000);
        System.out.println("hello");
        System.out.println(lru.get("1"));
        System.out.println(lru.get("2"));
        System.out.println(lru.get("3"));

    }
}
