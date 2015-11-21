package jc.house.activities;

import android.os.Bundle;
import android.app.Activity;
import android.view.Window;

import jc.house.R;
import jc.house.views.TitleBar;

public class CompanyIntroductionActivity extends Activity {
    private TitleBar titleBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_company_introduction);
        this.titleBar = (TitleBar) this.findViewById(R.id.titlebar);
        this.titleBar.setTitle("公司简介");
    }

}
