package com.proxy.cache;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class MemoryCache<K, V> {
    private Map<K, CacheObject> cacheMap = new HashMap<>();
    private long timeToLive;
    private boolean running = false;

    protected class CacheObject {
        public long lastChange;
        public V value;

        protected CacheObject(V value) {
            this.lastChange = System.currentTimeMillis();
            this.value = value;
        }
    }


    public MemoryCache(long timeToLive, final long verificationInterval) {
        this.timeToLive = timeToLive;

        if(timeToLive > 0 && verificationInterval > 0) {
            running = true;
            startVerificationThread(verificationInterval);
        } else {
            log.error("Incorrect cache inputs.");
        }
    }

    private void startVerificationThread(final long verificationInterval) {
        new Thread(() -> {
            while (running) {
                try {
                    Thread.sleep(verificationInterval * 1000);
                } catch (InterruptedException ex) {
                }
                cleanup();
            }
        });
    }

    public void put(K key, V value) {
        synchronized (cacheMap) {
            cacheMap.put(key, new CacheObject(value));
        }
    }

    public V get(K key) {
        synchronized (cacheMap) {
            CacheObject object = cacheMap.get(key);
            if(object == null) {
                return null;
            } else {
                object.lastChange = System.currentTimeMillis();
                return object.value;
            }
        }
    }

    public boolean containsKey(K key) {
        synchronized (cacheMap) {
            return cacheMap.containsKey(key);
        }
    }

    public void remove(K key) {
        synchronized (cacheMap) {
            cacheMap.remove(key);
        }
    }

    public void cleanup() {
        long timeNow = System.currentTimeMillis();
        ArrayList<K> keyToDelete = null;

        synchronized (cacheMap) {

            keyToDelete = new ArrayList<>();
            CacheObject cacheObject;

            for (Map.Entry<K, CacheObject> object: cacheMap.entrySet()) {
                cacheObject = object.getValue();

                if(cacheObject != null && (timeNow > (timeToLive + cacheObject.lastChange))) {
                    keyToDelete.add(object.getKey());
                }
            }

            for (K key : keyToDelete) {
                synchronized (cacheMap) {
                    cacheMap.remove(key);
                }

                Thread.yield();
            }
        }
    }

    public void disposeCache() {
        this.running = false;
    }
}
