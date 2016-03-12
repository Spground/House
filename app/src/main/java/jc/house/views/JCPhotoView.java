package jc.house.views;

import android.content.Context;
import android.util.AttributeSet;

import uk.co.senab.photoview.PhotoView;

/**
 * Created by hzj on 2016/3/10.
 */
public class JCPhotoView extends PhotoView {
    public JCPhotoView(Context context) {
        this(context, null);
    }

    public JCPhotoView(Context context, AttributeSet attr) {
        this(context, attr, 0);
    }

    public JCPhotoView(Context context, AttributeSet attr, int defStyle) {
        super(context, attr, defStyle);
        init();
    }
}
