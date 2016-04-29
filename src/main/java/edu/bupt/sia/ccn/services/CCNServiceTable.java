package edu.bupt.sia.ccn.services;

/**
 * Created by yangkuang on 16-4-15.
 */

import java.util.LinkedHashMap;
import java.util.Collection;
import java.util.Map;
import java.util.ArrayList;


public class CCNServiceTable<K,V> {

    private static final float   hashTableLoadFactor = 1; //default = 0.75f

    private LinkedHashMap<K,V>   map;
    private int                  cacheSize;

    public CCNServiceTable (int cacheSize) {
        this.cacheSize = cacheSize;
        int hashTableCapacity = (int)Math.ceil(cacheSize / hashTableLoadFactor) + 1;
        map = new LinkedHashMap<K,V>(hashTableCapacity, hashTableLoadFactor, true) {
            private static final long serialVersionUID = 1;
            @Override protected boolean removeEldestEntry (Map.Entry<K,V> eldest) {
                return size() > CCNServiceTable.this.cacheSize;
            }
        };
    }

    public synchronized V get (K key) {
        return map.get(key);
    }

    public synchronized void put (K key, V value) {
        map.put (key, value);
    }

    public synchronized void clear() {
        map.clear();
    }

    public synchronized void delete(K key) {
        map.remove(key);
    }

    public synchronized int usedSize() {
        return map.size();
    }

    public synchronized Collection<Map.Entry<K,V>> getAll() {
        return new ArrayList<Map.Entry<K,V>>(map.entrySet());
    }

    public LinkedHashMap<K, V> getMap() {
        return map;
    }

}
