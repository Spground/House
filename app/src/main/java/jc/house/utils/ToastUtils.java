package jc.house.utils;

import android.content.Context;
import android.widget.Toast;

import jc.house.global.Constants;

/**
 * Created by wujie on 2015/11/2.
 */
public class ToastUtils {
    public static void show(Context ctx, String msg){
        if(Constants.DEBUG)
            Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
    }
}
