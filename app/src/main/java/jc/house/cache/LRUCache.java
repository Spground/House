package jc.house.cache;

import android.graphics.Bitmap;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by hzj on 2016/3/30.
 */
public class LRUCache implements Cache {

    private final LinkedHashMap<String, Bitmap> map;
    private int maxSize;
    private int size;
    private int putCount, hitCount, missCount, recyleCount;

    public LRUCache(int maxSize) {
        if (maxSize <= 0) {
            throw new IllegalArgumentException("maxSize must be positive");
        }
        this.maxSize = maxSize;
        this.map = new LinkedHashMap<String, Bitmap>(0, 0.75f, true);
    }

    @Override
    public Bitmap get(String key) {
        if (null == key) {
            throw new NullPointerException("key should not be null");
        }
        synchronized (this) {
            Bitmap value = map.get(key);
            if (null != value) {
                hitCount++;
                return value;
            }
            missCount++;
        }
        return null;
    }

    @Override
    public void set(String key, Bitmap value) {
        if (null == key || null != value) {
            throw new NullPointerException("key and value should not be null");
        }
        synchronized (this) {
            Bitmap previous = map.put(key, value);
            size++;
            putCount++;
            if (null != previous) {
                size--;
            }
            trimSize();
        }
    }

    private void trimSize() {
        synchronized (this) {
            while (true) {
                if (size <= maxSize || map.isEmpty()) {
                    break;
                }
                Map.Entry<String, Bitmap> entry = map.entrySet().iterator().next();
//                Bitmap value = entry.getValue();
                String key = entry.getKey();
                size--;
                map.remove(key);
                recyleCount++;
            }
        }
    }

    @Override
    public void clear() {
        synchronized (this) {
            map.clear();
            size = 0;
            putCount = 0;
            hitCount = 0;
            missCount = 0;
            recyleCount = 0;
        }
    }

    @Override
    public synchronized int maxSize() {
        return maxSize;
    }

    @Override
    public synchronized int size() {
        return size;
    }

    public int getPutCount() {
        return putCount;
    }

    public synchronized int getHitCount() {
        return hitCount;
    }

    public synchronized int getMissCount() {
        return missCount;
    }

    public synchronized int getRecyleCount() {
        return recyleCount;
    }
}
