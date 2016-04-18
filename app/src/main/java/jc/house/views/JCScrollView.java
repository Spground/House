package jc.house.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by hzj on 2016/4/18.
 */
public class JCScrollView extends ScrollView {
    public JCScrollView(Context context) {
        super(context);
    }

    public JCScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public JCScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        super.onInterceptTouchEvent(ev);
        return false;
//        return super.onInterceptTouchEvent(ev);
    }
}
