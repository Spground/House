package jc.house.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import jc.house.R;
import jc.house.global.Constants;
import jc.house.utils.ToastUtils;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class PhotoViewActivity extends BaseActivity {
    private final static String TAG = "PhotoViewActivity";
    public final static String FLAG_IMAGE_URL = "image_url";

    private PhotoView photoView;
    private PhotoViewAttacher mAttacher;
    private String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setJCContentView(R.layout.activity_photo_view);
        setTitleBarTitle("查看原图");
        this.imageUrl = getIntent().getStringExtra(FLAG_IMAGE_URL);
        if (null != imageUrl) {
            initView();
        }
    }

    /**
     * init view
     */
    private void initView() {
        this.photoView = (PhotoView) this.findViewById(R.id.photoView);
        this.mAttacher = new PhotoViewAttacher(this.photoView);
        this.mAttacher.setZoomable(true);
        this.mAttacher.setScaleLevels(0.3f, 0.5f, 0.7f);
        this.mAttacher.setMaximumScale(3.0f);
        this.mAttacher.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        this.mAttacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {

            }
        });
        showDialog();
        //show image
        /**debug indicators, should be deleted at production**/
        Picasso.with(this).setIndicatorsEnabled(Constants.PRODUCT);
        Picasso.with(this).setLoggingEnabled(Constants.DEBUG);
        Picasso.with(this)
                .load(this.imageUrl)
                .fit()
                .centerInside()
                .placeholder(R.drawable.jc_default_image)
                .error(R.drawable.failure_image_red)
                .into(photoView, new Callback() {
                    @Override
                    public void onSuccess() {
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onError() {
                        progressDialog.dismiss();
                        ToastUtils.show(PhotoViewActivity.this, "oh oh ,查看全图失败");
                    }
                });
        this.mAttacher.update();
    }
}
