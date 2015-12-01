package jc.house.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Window;

import jc.house.R;
import jc.house.views.TitleBar;

public class BaseActivity extends Activity {

    protected ProgressDialog progressDialog;
    protected TitleBar titleBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        this.progressDialog = new ProgressDialog(this, R.style.CustomProgressDialog);
        this.progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        this.progressDialog.setMessage("正在加载中...");
        this.progressDialog.setCancelable(true);
        this.progressDialog.setCanceledOnTouchOutside(true);
    }

    /**
     * set div_titlebar
     * @param title
     */
    protected void setTitleBarTitle(String title){
        //maybe is null
        titleBar = (TitleBar)findViewById(R.id.titlebar);
        if(this.titleBar != null)
            this.titleBar.setTitle(title);
    }
}
