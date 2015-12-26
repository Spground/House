package jc.house.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import jc.house.R;
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
        this.photoView = (PhotoView) this.findViewById(R.id.photoView);
        this.mAttacher = new PhotoViewAttacher(this.photoView);
        this.mAttacher.setZoomable(true);
        this.mAttacher.setMaximumScale(3.0f);
        this.mAttacher.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        this.mAttacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {

            }
        });
        showDialog();
        //show image
        Picasso.with(this).load(this.imageUrl).placeholder(R.drawable.failure_image_red).error(R.drawable.failure_image_red).into(photoView, new Callback() {
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
