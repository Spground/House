package jc.house.activities;

import android.os.Bundle;

import jc.house.R;

public class AboutUsActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        setTitleBarTitle("关于我们");
    }

}
