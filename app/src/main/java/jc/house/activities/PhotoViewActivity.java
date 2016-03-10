package jc.house.activities;

import android.os.Bundle;

import jc.house.R;
import jc.house.async.StringTask;
import jc.house.views.CircleView;

public class PhotoViewActivity extends BaseActivity {
    private final static String TAG = "PhotoViewActivity";
    public final static String FLAG_IMAGE_URL = "imageUrl";
    public final static String FLAG_CURRENT_INDEX = "currentIndex";
    public final static String FLAG_PICASSO = "picasso";
    private boolean isPicasso = true;

    private String[] imageUrls;
    private CircleView circleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setJCContentView(R.layout.activity_photo_view);
        setTitleBarTitle("查看原图");
        this.imageUrls = getIntent().getStringArrayExtra(FLAG_IMAGE_URL);
        int currentIndex = getIntent().getIntExtra(FLAG_CURRENT_INDEX, 0);
        isPicasso = getIntent().getBooleanExtra(FLAG_PICASSO, true);
        if (null != imageUrls && this.imageUrls.length > 0) {
            initView(currentIndex);
        }
    }

    /**
     * init view
     */
    private void initView(int currentIndex) {
        this.circleView = (CircleView)this.findViewById(R.id.photo_circleview);
        this.circleView.setAutoPlay(false);
        this.circleView.setPhotoViews(imageUrls, isPicasso);
        this.circleView.setShowIndex(currentIndex);
    }

    @Override
    public void finish() {
        this.circleView.cancelTimer();
        super.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
