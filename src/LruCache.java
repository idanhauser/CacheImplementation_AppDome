import java.util.*;
import java.util.concurrent.TimeUnit;

public class LruCache<K, V> {

    // Capacity of LRUCache
    private final int capacity;

    // Interval for testing existence of an object
    private final int sleepTime;
    // Cache expire time
    private final int expireTime;
    private final TimeUnit timeUnit;

    // Time unit like Seconds, Minutes, Hours etc.
    // Map for key and DoublyLinkedList node mapping
    private final HashMap<K, DoublyLinkedListNode<K, V>> nodeMap;
    CacheList<K, V> cacheList;
    // Current size of LRUCache
    private int currentSize;

    public LruCache(int capacity, int expireTime, TimeUnit timeUnit) {
        currentSize = 0;
        this.expireTime = expireTime;
        this.timeUnit = timeUnit;
        this.capacity = capacity;
        nodeMap = new HashMap<>();
        cacheList = new CacheList<>(null, null);

        switch (this.timeUnit) {
            case SECONDS:
                this.sleepTime = expireTime * 1000;
                break;
            case MINUTES:
                this.sleepTime = expireTime * 60000;
                break;
            case HOURS:
                this.sleepTime = expireTime * 3600000;
                break;
            default://MILLISECONDS
                this.sleepTime = expireTime;
                break;
        }

        Thread threadCleaner = new Thread(() -> {
            List<K> deleteKey;

            try {
                while (true) {
                    System.out.println("CacheCleaner scanning for expired objects...");
                    synchronized (nodeMap) {
                        deleteKey = new ArrayList<>((nodeMap.size() / 2) + 1);
                        Set<K> keySet = nodeMap.keySet();
                        for (K key : keySet) {
                            DoublyLinkedListNode<K, V> value = nodeMap.get(key);
                            if (value.isExpired()) {
                                deleteKey.add(key);
                                System.out.println("CacheCleaner Running. Found an expired object in the Cache : "
                                        + value.value);
                            }
                        }
                    }

                    for (K key : deleteKey) {
                        synchronized (nodeMap) {
                            System.out.println("CacheCleaner removed an expired object from the Cache : "
                                    + nodeMap.get(key).value);
                            nodeMap.remove(key);
                        }

                        Thread.yield();
                    }

                    Thread.sleep(sleepTime);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        threadCleaner.setPriority(Thread.MIN_PRIORITY);
        threadCleaner.start();
    }

    // Add an item to LRUCache
    public void put(K key, V value) {
        synchronized (nodeMap) {
            System.out.println("Adding new object..." + value);
            if (nodeMap.containsKey(key)) {
                DoublyLinkedListNode<K, V> node = nodeMap.get(key);
                node.value = value;
                cacheList.bringItemToFront(node);
                nodeMap.put(key, node);

            } else {
                DoublyLinkedListNode<K, V> nodeToInsert = new DoublyLinkedListNode<>(key, value, timeUnit, expireTime);
                if (currentSize < capacity) {
                    cacheList.addItemToFront(nodeToInsert);
                    nodeMap.put(key, nodeToInsert);

                    currentSize++;
                } else {
                    cacheList.removeLastNode();
                    nodeMap.remove(cacheList.end.key);
                    cacheList.addItemToFront(nodeToInsert);
                    nodeMap.put(key, nodeToInsert);

                }
            }
        }
    }

    // Get an item from LRUCache
    public V get(K key) {
        synchronized (nodeMap) {
            if (nodeMap.containsKey(key)) {
                DoublyLinkedListNode<K, V> node = nodeMap.get(key);
                cacheList.bringItemToFront(node);
                nodeMap.put(key, node);
                return node.value;
            } else {
                return null;
            }
        }
    }

    public void delete(K key) {
        synchronized (nodeMap) {
            if (nodeMap.containsKey(key)) {
                DoublyLinkedListNode<K, V> node = nodeMap.get(key);
                nodeMap.remove(key);
            }
        }
    }


}