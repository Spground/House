package jc.house.cache;

import android.graphics.Bitmap;

/**
 * Created by hzj on 2016/3/30.
 */
public interface Cache {
    Bitmap get(String key);
    void set(String key, Bitmap value);
    void clear();
    int maxSize();
    int size();
}
