package jc.house.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;

import jc.house.global.MApplication;
import jc.house.views.TitleBar;

public class BaseActivity extends Activity {

    protected ProgressDialog progressDialog;
    protected TitleBar titleBar;
    protected LayoutInflater mInflater;
    protected MApplication mApplication;
    protected LinearLayout baseLayout;
    protected RelativeLayout contentLayout;
    private AsyncHttpClient mClient = null;

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
        this.mApplication = (MApplication)this.getApplication();
        this.titleBar = new TitleBar(this);
        this.titleBar.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        this.baseLayout = new LinearLayout(this);
        this.baseLayout.setOrientation(LinearLayout.VERTICAL);
        contentLayout = new RelativeLayout(this);
        baseLayout.addView(titleBar, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        titleBar.setVisibility(View.GONE);
        LinearLayout.LayoutParams layoutContent = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        baseLayout.addView(contentLayout, layoutContent);
        setContentView(baseLayout, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
    }

    public void setJCContentView(View contentView) {
        contentLayout.removeAllViews();
        contentLayout.addView(contentView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
    }

    public void setJCContentView(int resId) {
        setJCContentView(mInflater.inflate(resId, null));
    }

    /**
     * set div_titlebar
     *
     * @param title
     */
    protected void setTitleBarTitle(String title) {
        this.titleBar.setTitle(title);
        setTitleBarVisible();
    }

    protected AsyncHttpClient getHttpClient() {
        if (null == this.mClient) {
            this.mClient = new AsyncHttpClient();
            this.mClient.setTimeout(5 * 1000);
        }
        return this.mClient;
    }

    public void setTitleBarVisible() {
        this.titleBar.setVisibility(View.VISIBLE);
    }

    public void setTitleBarHide() {
        this.titleBar.setVisibility(View.GONE);
    }

    protected void ToastS(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    protected void ToastL(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
