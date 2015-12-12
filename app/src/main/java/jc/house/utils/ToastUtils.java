package jc.house.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import jc.house.R;
import jc.house.global.Constants;

/**
 * Created by wujie on 2015/11/app.
 */
public class ToastUtils {
    private static final boolean DEBUG = Constants.DEBUG;
    public static void show(Context ctx, String msg) {
        if(ctx == null || msg == null)
            return;
        doShowToast(ctx, msg);
    }

    public static void debugShow(Context ctx, String msg) {
        if(ctx == null || msg == null)
            return;
        if(DEBUG)
            doShowToast(ctx, msg);
    }

    private static void doShowToast(Context ctx, String msg) {
        View toastView = LayoutInflater.from(ctx).inflate(R.layout.jc_default_toast_view, null);
        Toast toast = Toast.makeText(ctx, null, Toast.LENGTH_SHORT);
        if(toastView != null) {
            ((TextView)toastView.findViewById(R.id.toast_message)).setText(msg);
            toast.setView(toastView);
        }
        toast.show();
    }
}
