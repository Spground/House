package jc.house.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import jc.house.R;
import jc.house.models.House;
import jc.house.models.HouseDetail;
import jc.house.views.CircleView;

public class PhotoViewActivity extends BaseActivity {
    private final static String TAG = "PhotoViewActivity";
    public final static String FLAG_IMAGE_URL = "imageUrl";
    public final static String FLAG_CURRENT_INDEX = "currentIndex";
    public final static String FLAG_IS_HOUSE = "isHouse";
    private String[] imageUrls;
    private CircleView circleView;
    private boolean isHouse;
    private HouseDetail house;
    private String hxID;
    private String nickName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setJCContentView(R.layout.activity_photo_view);
        setTitleBarTitle("查看原图");
        this.imageUrls = getIntent().getStringArrayExtra(FLAG_IMAGE_URL);
        int currentIndex = getIntent().getIntExtra(FLAG_CURRENT_INDEX, 0);
        isHouse = getIntent().getBooleanExtra(FLAG_IS_HOUSE, false);
        if (isHouse) {
            this.hideTitleBar();
            this.house = getIntent().getParcelableExtra(HouseDetailActivity.FLAG_HOUSE_DETAIL);
            this.hxID = getIntent().getStringExtra(HouseDetailActivity.FLAG_HELPER_ID);
            this.nickName = getIntent().getStringExtra(HouseDetailActivity.FLAG_HELPER_NAME);
        }
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
        if (isHouse) {
            this.circleView.setPhotoViews(imageUrls, ImageView.ScaleType.FIT_XY);
        } else {
            this.circleView.setPhotoViews(imageUrls, ImageView.ScaleType.FIT_CENTER);
        }
        this.circleView.setShowIndex(currentIndex);
        if (isHouse) {
            this.circleView.setOnCircleViewItemClickListener(new CircleView.CircleViewOnClickListener() {
                @Override
                public void onCircleViewItemClick(View v, int index) {
                    Intent intent = new Intent(PhotoViewActivity.this, HouseDetailActivity.class);
                    intent.putExtra(HouseDetailActivity.FLAG_HOUSE_DETAIL, house);
                    intent.putExtra(HouseDetailActivity.FLAG_HELPER_NAME, nickName);
                    intent.putExtra(HouseDetailActivity.FLAG_HELPER_ID, hxID);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }

    @Override
    public void finish() {
        this.circleView.cancelTimer();
        super.finish();
    }

}
