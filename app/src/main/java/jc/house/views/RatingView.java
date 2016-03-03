package jc.house.views;


import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import jc.house.R;

/**
 * Created by hzj on 2015/12/8.
 */
public class RatingView extends LinearLayout {

    private int ratingRedNumber;
    private int spacing;
    private static final int RatingRedResID = R.drawable.rating_red;
    private static final int RatingWhiteID = R.drawable.rating_white;
    private static final int ALL_NUMBER = 5;
    private Context context;
    private boolean hasInit;
    public RatingView(Context context) {
        this(context, null);
    }
    public RatingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RatingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        this.context = context;
        this.setLayoutParams(new LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        this.ratingRedNumber = 0;
        this.spacing = 0;
        this.hasInit = false;
    }

    public void setParams(int ratingRedNumber, int spacing) {
        if (this.ratingRedNumber == ratingRedNumber && ratingRedNumber > 0) {
            return;
        }
        if (ratingRedNumber > ALL_NUMBER) {
            ratingRedNumber = ALL_NUMBER;
        }
        this.spacing = spacing <= 0 ? 0 : spacing;
        this.ratingRedNumber = ratingRedNumber < 0 ? 0 : ratingRedNumber;
        if (!hasInit) {
            this.removeAllViews();
        }
        for (int i = 0; i < ALL_NUMBER; i++) {
            ImageView imageView;
            if (!hasInit) {
                imageView = new ImageView(context);
                imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                if (i > 0) {
                    imageView.setPadding(this.spacing, 0, 0, 0);
                }
                this.addView(imageView);
            } else {
                imageView = (ImageView)(this.getChildAt(i));
            }
            if (i < this.ratingRedNumber) {
                imageView.setImageResource(RatingRedResID);
            } else {
                imageView.setImageResource(RatingWhiteID);
            }
        }
        this.hasInit = true;
    }

}
