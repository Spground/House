package jc.house.activities;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.ImageView;

import jc.house.R;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class PhotoViewActivity extends Activity {
    private PhotoView photoView;
    private PhotoViewAttacher mAttacher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view);
        this.photoView = (PhotoView)this.findViewById(R.id.photoView);
        this.mAttacher = new PhotoViewAttacher(this.photoView);
        this.mAttacher.setZoomable(true);
        this.mAttacher.setMaximumScale(3.0f);
        this.mAttacher.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        this.photoView.setImageResource(R.drawable.user_mao);
        this.mAttacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                finish();
            }
        });
        this.mAttacher.update();
    }

}
