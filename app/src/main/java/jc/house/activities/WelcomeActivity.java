package jc.house.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Scroller;

import jc.house.R;

public class WelcomeActivity extends Activity {
    Handler mHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 0x123){
                startActivity(new Intent(WelcomeActivity.this, HomeActivity.class));
                WelcomeActivity.this.finish();
            }
        }
    } ;

    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_welcome);
        imageView = (ImageView)findViewById(R.id.id_imageview);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_welcome_image);
        animation.setFillAfter(true);
        imageView.startAnimation(animation);
        mHandler.sendEmptyMessageDelayed(0x123, 3 * 1000);
        Scroller scroller = new Scroller(this);
    }
}
