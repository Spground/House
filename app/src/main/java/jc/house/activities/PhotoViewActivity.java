package jc.house.activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import jc.house.R;
import jc.house.utils.LogUtils;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class PhotoViewActivity extends BaseActivity {
    private final static String TAG = "PhotoViewActivity";
    private final static String FLAG_IMAGE_URL = "image_url";

    private PhotoView photoView;
    private PhotoViewAttacher mAttacher;

    private String imageUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setJCContentView(R.layout.activity_photo_view);
        setTitleBarTitle("查看原图");
        this.imageUrl = getIntent().getStringExtra(FLAG_IMAGE_URL);
        initView();
    }

    /**
     * init view
     */
    private void initView() {
        this.photoView = (PhotoView)this.findViewById(R.id.photoView);
        this.mAttacher = new PhotoViewAttacher(this.photoView);
        this.mAttacher.setZoomable(true);
        this.mAttacher.setMaximumScale(3.0f);
        this.mAttacher.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        this.mAttacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
            }
        });
        progressDialog.show();
        LogUtils.debug(TAG, "Image Loading url is " + this.imageUrl);
        //show image
        Picasso.with(this).load(this.imageUrl).into(photoView, new Callback() {
            @Override
            public void onSuccess() {
                progressDialog.dismiss();
            }

            @Override
            public void onError() {
                progressDialog.dismiss();
            }
        });
        this.mAttacher.update();
    }
}
