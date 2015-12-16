package jc.house.activities;

import android.os.Bundle;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import jc.house.R;
import jc.house.global.Constants;

public class AboutUsActivity extends BaseActivity {
    private ImageView appImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setJCContentView(R.layout.activity_about_us);
        setTitleBarTitle("关于我们");
        this.appImageView = (ImageView)this.findViewById(R.id.app_icon);
        Picasso.with(this).load(Constants.IMAGE_URL + "qq.png").into(appImageView);
    }
}
