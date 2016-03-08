package jc.house.utils;

import android.content.Context;
import android.content.SharedPreferences;

import jc.house.async.MThreadPool;
import jc.house.models.BaseModel;

/**
 * Created by hzj on 2016/3/8.
 */
public class SP {
    public static final  String SP = "JC_HOUSE";
    private static SP singleton;
    private SharedPreferences sp;
    public static SP with(Context context) {
        if (null == singleton) {
            synchronized (SP.class) {
                if (null == singleton) {
                    singleton = new SP(context);
                }
            }
        }
        return singleton;
    }

    private SP(Context context) {
        sp = context.getSharedPreferences(SP, Context.MODE_PRIVATE);
    }

    public void saveJsonString(final String content, final Class<? extends BaseModel> cls) {
        if (StringUtils.strEmpty(content) || null == cls) {
            return;
        }
        saveString(cls.toString(), content);
    }
    public String getJsonString(Class<? extends BaseModel> cls) {
        if (null == cls) {
            throw new NullPointerException("cls should not be null");
        }
        return getString(cls.toString());
    }

    public void saveString(final String key, final String content) {
        if (StringUtils.strEmpty(key) || StringUtils.strEmpty(content)) {
            return;
        }
        MThreadPool.getInstance().submit(new Runnable() {
            @Override
            public void run() {
                SharedPreferences.Editor editor = sp.edit();
                editor.putString(key, content);
                editor.apply();
            }
        });
    }

    public String getString(String key) {
        if (StringUtils.strEmpty(key)) {
            return "";
        }
        return sp.getString(key, "");
    }
}
