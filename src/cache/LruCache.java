package cache;

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
    private final HashMap<K, CacheElement<K, V>> nodeMap;
    CacheList<K, V> cacheList;
    // Current size of LRUCache
    private int currentSize;

    public LruCache(int capacity, int expireTime, TimeUnit timeUnit) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("capacity <= 0");
        }

        currentSize = 0;
        this.expireTime = expireTime;
        this.timeUnit = timeUnit;
        this.capacity = capacity;
        nodeMap = new HashMap<>();
        cacheList = new CacheList<>(null, null);


        if (expireTime > 0) {
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
                                CacheElement<K, V> value = nodeMap.get(key);
                                if (value.isExpired()) {
                                    deleteKey.add(key);
                                    System.out.println("CacheCleaner Running. Found an expired object in the Cache : " + value.value);
                                }
                            }
                        }

                        for (K key : deleteKey) {
                            synchronized (nodeMap) {
                                System.out.println("CacheCleaner removed an expired object from the Cache : " + nodeMap.get(key).value);
                                currentSize--;
                                cacheList.removeNode(nodeMap.get(key));
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

            if (expireTime > 0) {
                threadCleaner.setPriority(Thread.MIN_PRIORITY);
                threadCleaner.start();
            }
        } else {
            sleepTime = 0;
        }
    }

    // Add an item to LRUCache
    public void put(K key, V value) {
        synchronized (nodeMap) {
            Objects.requireNonNull(key, "key == null");
            Objects.requireNonNull(value, "value == null");
            System.out.println("Adding new object..." + value);
            if (nodeMap.containsKey(key)) {
                CacheElement<K, V> node = nodeMap.get(key);
                node.value = value;
                cacheList.bringItemToFront(node);
                //nodeMap.put(key, node); // commented - if it contains, i dont need to put it again.

            } else {
                CacheElement<K, V> nodeToInsert = new CacheElement<>(key, value, timeUnit, expireTime);
                if (currentSize < capacity) {
                    cacheList.addItemToFront(nodeToInsert);
                    nodeMap.put(key, nodeToInsert);
                    currentSize++;
                } else {
                    nodeMap.remove(cacheList.end.key);
                    cacheList.removeLastNode();
                    cacheList.addItemToFront(nodeToInsert);
                    nodeMap.put(key, nodeToInsert);
                }
                nodeMap.put(key, nodeToInsert);
            }
        }
    }

    // Get an item from LRUCache
    public V get(K key) {
        Objects.requireNonNull(key, "key == null");
        synchronized (nodeMap) {
            if (nodeMap.containsKey(key)) {
                CacheElement<K, V> node = nodeMap.get(key);
                cacheList.bringItemToFront(node);
                nodeMap.put(key, node);
                return node.value;
            } else {
                return null;
            }
        }
    }

    public void delete(K key) {
        Objects.requireNonNull(key, "key == null");
        synchronized (nodeMap) {
            if (nodeMap.containsKey(key)) {
                CacheElement<K, V> node = nodeMap.get(key);
                System.out.println("Deleting object..." + node.value);
                currentSize--;
                nodeMap.remove(key);
                cacheList.removeNode(node);

            }
        }
    }


}