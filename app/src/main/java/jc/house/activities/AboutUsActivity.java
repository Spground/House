package jc.house.activities;

import android.os.Bundle;
import android.widget.ImageView;

import jc.house.R;

public class AboutUsActivity extends BaseActivity {
    private ImageView appImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setJCContentView(R.layout.activity_about_us);
        setTitleBarTitle("关于我们");
        this.appImageView = (ImageView)this.findViewById(R.id.app_icon);
    }
}
