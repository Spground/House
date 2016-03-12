package jc.house.global;

import android.content.Context;

import com.squareup.picasso.Picasso;

/**
 * Created by WuJie on 2016/3/12.
 */
public class MPicasso {
    private static Picasso instance = null;

    private MPicasso() {

    }

    public static Picasso getInstance() {
        if (instance == null)
            throw new RuntimeException("you have not initialed the instance");
        return instance;
    }

    public static void initPicasso(Context ctx) {
        if (instance != null)
            throw new RuntimeException("you have initialed the instance");
        Picasso.Builder builder = new Picasso.Builder(ctx);
        if (Constants.DEBUG)
            builder.indicatorsEnabled(true);
        instance = builder.build();
        Picasso.setSingletonInstance(instance);
    }
}
