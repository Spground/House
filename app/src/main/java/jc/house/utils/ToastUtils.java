package jc.house.utils;

import android.content.Context;
import android.widget.Toast;

import jc.house.global.Constants;

/**
 * Created by wujie on 2015/11/app.
 */
public class ToastUtils {
    private static final boolean DEBUG = Constants.DEBUG;
    public static void show(Context ctx,String msg){
        if(ctx == null || msg == null)
            return;
        Toast.makeText(ctx,msg,Toast.LENGTH_SHORT).show();
    }

    public static void debugShow(Context ctx,String msg){
        if(ctx == null || msg == null)
            return;
        if(DEBUG)
            Toast.makeText(ctx,msg,Toast.LENGTH_SHORT).show();
    }
}
