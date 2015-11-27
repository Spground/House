package jc.house.activities;

import android.os.Bundle;

import jc.house.R;

public class CompanyIntroductionActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_introduction);
        setTitleBarTitle("公司简介");
    }
}
