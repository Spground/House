package jc.house.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
import jc.house.R;

/**
 * Created by hzj on 2015/11/19.
 */
public class JCTextView extends TextView {
    private int index;
    private boolean selected;
    private int normalDrawable, selectedDrawable;
    public JCTextView(Context context) {
        super(context);
        initView();
    }

    public JCTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public JCTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        this.index = 0;
        this.selected = false;
        this.normalDrawable = R.drawable.jctextview_normal;
        this.selectedDrawable = R.drawable.jctextview_selected;
        this.setBackgroundResource(normalDrawable);
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;
        if (selected) {
            this.setBackgroundResource(selectedDrawable);
        } else {
            this.setBackgroundResource(normalDrawable);
        }
    }
}
