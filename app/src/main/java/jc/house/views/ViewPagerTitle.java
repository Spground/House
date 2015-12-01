package jc.house.views;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.TextView;
import jc.house.R;

/**
 * Created by hzj on 2015/11/19.
 */
public class ViewPagerTitle extends TextView {
    private int index;
    private boolean selected;
    private int normalDrawable, selectedDrawable;
    private int normalTextColor = Color.rgb(160, 160, 160);
    private int selectedTextColor = Color.rgb(255, 255, 255);
    public ViewPagerTitle(Context context) {
        super(context);
        initView();
    }

    public ViewPagerTitle(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ViewPagerTitle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        this.index = 0;
        this.selected = false;
        this.normalDrawable = R.drawable.jctextview_normal;
        this.selectedDrawable = R.drawable.jctextview_selected;
        this.setTextColor(normalTextColor);
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
        if (this.selected) {
            this.setBackgroundResource(selectedDrawable);
            this.setTextColor(selectedTextColor);
        } else {
            this.setBackgroundResource(normalDrawable);
            this.setTextColor(normalTextColor);
        }
    }
}
