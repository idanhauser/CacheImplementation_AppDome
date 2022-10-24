import cache.LruCache;


import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        LruCache<String, Integer> lru = new LruCache<String, Integer>(3, 5, TimeUnit.HOURS);

        lru.put(String.valueOf(1), 2);
        lru.put(String.valueOf(2), 2 * 2);
        lru.put(String.valueOf(3), 3 * 2);




        System.out.println("getting 1 should print 2 :" + lru.get("1"));
        System.out.println("getting 2 should print 4 :" + lru.get("2"));
        System.out.println("getting 3 should print 8 :" + lru.get("3"));
        lru.put(String.valueOf(4), 4 * 2);
        System.out.println("getting 4 should print 8 :" + lru.get("4"));

    }
}
