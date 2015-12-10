package jc.house.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Window;
import android.widget.Toast;

import jc.house.R;
import jc.house.views.TitleBar;

public class BaseActivity extends Activity {

    protected ProgressDialog progressDialog;
    protected TitleBar titleBar;
    protected LayoutInflater mInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        this.progressDialog = new ProgressDialog(this);
        this.progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        this.progressDialog.setMessage("正在加载中...");
        this.progressDialog.setCancelable(true);
        this.progressDialog.setCanceledOnTouchOutside(true);
        this.mInflater = this.getLayoutInflater();
    }

    /**
     * set div_titlebar
     *
     * @param title
     */
    protected void setTitleBarTitle(String title) {
        //maybe is null
        titleBar = (TitleBar) findViewById(R.id.titlebar);
        if (this.titleBar != null)
            this.titleBar.setTitle(title);
    }

    protected void ToastS(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    protected void ToastL(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
