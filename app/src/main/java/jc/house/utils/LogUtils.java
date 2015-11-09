package jc.house.utils;

import android.util.Log;

import jc.house.global.Constants;

/**
 * Created by wujie on 2015/11/2.
 */
public class LogUtils {
    private static final boolean DEBUG = Constants.DEBUG;
    public static void debug(String str){
        if(DEBUG)
            Log.v("===JC===",str);
    }

    public static void debug(String tag,String str){
        if(DEBUG)
            Log.v("===" + tag + "===",str);
    }
}
