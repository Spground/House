package jc.house.utils;

import android.util.Log;

import jc.house.global.Constants;

/**
 * Created by wujie on 2015/11/app.
 */
public final class LogUtils {
    private static final boolean DEBUG = Constants.DEBUG;
    
    public static void debug(String tag){
        if(DEBUG)
            Log.v("---jc---", tag);
    }

    public static void debug(String tag, String str){
        if(DEBUG)
            Log.v(tag, str);
    }
}
