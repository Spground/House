package jc.house.activities;

import android.os.Bundle;
import android.view.Window;

import jc.house.R;

public class UserFeedbackActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feedback);
        setTitleBarTitle("用户反馈");
    }
}
