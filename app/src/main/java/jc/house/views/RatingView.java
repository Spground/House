package jc.house.views;


import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import jc.house.R;

/**
 * Created by hzj on 2015/12/8.
 */
public class RatingView extends LinearLayout {

    private int ratingRedNumbers;
    private int spacing;
    private static final int RatingRedResID = R.drawable.rating_red;
    private static final int RatingWhiteID = R.drawable.rating_white;
    private static final int ALL_NUMBERS = 5;
    private Context context;
    public RatingView(Context context) {
        super(context);
        initView(context);
    }
    public RatingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
        initView(context);
    }

    public RatingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        this.context = context;
        this.setLayoutParams(new LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        this.ratingRedNumbers = 0;
        this.spacing = 0;

    }

    public void setParams(int ratingRedNumbers, int spacing) {
        if (this.ratingRedNumbers == ratingRedNumbers) {
            return;
        }
        if (ratingRedNumbers > ALL_NUMBERS) {
            ratingRedNumbers = ALL_NUMBERS;
        }
        this.spacing = spacing <= 0 ? 0 : spacing;
        this.ratingRedNumbers = ratingRedNumbers < 0 ? 0 : ratingRedNumbers;
        this.removeAllViews();
        int redCount = 0;
        for (int i = 0; i < ALL_NUMBERS; i++) {
            ImageView imageView = new ImageView(context);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            if (i > 0) {
                imageView.setPadding(this.spacing, 0, 0, 0);
            }
            if (redCount < this.ratingRedNumbers) {
                imageView.setImageResource(RatingRedResID);
                redCount++;
            } else {
                imageView.setImageResource(RatingWhiteID);
            }
            this.addView(imageView);
        }
    }

}
