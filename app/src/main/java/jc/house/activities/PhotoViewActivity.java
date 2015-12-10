package jc.house.activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import jc.house.R;
import jc.house.utils.LogUtils;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class PhotoViewActivity extends BaseActivity {
    private final static String TAG = "PhotoViewActivity";

    private PhotoView photoView;
    private PhotoViewAttacher mAttacher;

    private DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageOnFail(R.drawable.app)
            .showImageForEmptyUri(R.drawable.app).build();
    private String imageUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setJCContentView(R.layout.activity_photo_view);
        setTitleBarTitle("查看原图");
        this.imageUrl = getIntent().getStringExtra("image_url");
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
        ImageLoader.getInstance().displayImage(this.imageUrl, this.photoView, options, new SimpleImageLoadingListener(){
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                super.onLoadingStarted(imageUri, view);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                super.onLoadingCancelled(imageUri, view);
                LogUtils.debug(TAG, "Image Loading is canceled");
                progressDialog.dismiss();
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                super.onLoadingFailed(imageUri, view, failReason);
                LogUtils.debug(TAG, "Image Loading is failed");
                progressDialog.dismiss();
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                super.onLoadingComplete(imageUri, view, loadedImage);
                LogUtils.debug(TAG, "Image Loading is completed");
                progressDialog.dismiss();
            }
        });
        this.mAttacher.update();
    }
}
