package jc.house.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import jc.house.global.Constants;
import jc.house.global.MApplication;
import jc.house.utils.LogUtils;
import jc.house.views.TitleBar;

public class BaseActivity extends Activity {

    protected ProgressDialog progressDialog;
    protected TitleBar titleBar;
    protected LayoutInflater mInflater;
    protected MApplication mApplication;
    protected LinearLayout baseLayout;
    protected RelativeLayout contentLayout;
    private boolean isScrollRightBack;
    public static final boolean PRODUCING = Constants.PRODUCING;
    private float xPre = 0;
    private static final float MIN_DISTANCE = 150;
    private static final float MIN_SPEED = 200;
    private static final float LEFT_SPACE = 240;
    private VelocityTracker velocityTracker;

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
        this.isScrollRightBack = false;
        this.mApplication = (MApplication) this.getApplication();
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

    protected void showDialog() {
        this.progressDialog.show();
    }

    protected void hideDialog() {
        this.progressDialog.hide();
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

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (this.isScrollRightBack) {
            createVelocityTracker(event);
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    xPre = event.getRawX();
                    break;
                case MotionEvent.ACTION_MOVE:
                    float xCur = event.getRawX();
                    handleMove(xCur);
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                default:
                    xPre = 0;
                    recycleVelocityTracker();
                    break;
            }
        }
        LogUtils.debug("eventA + " + event.toString());
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        LogUtils.debug("eventB + " + event.toString());
        return super.onTouchEvent(event);
    }

    protected void ToastL(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    protected final void setScrollRightBack(boolean flag) {
        if (this.isScrollRightBack != flag) {
            this.isScrollRightBack = flag;
        }
    }

    private void handleMove(float xCur) {
        if (xPre <= LEFT_SPACE && (xCur - xPre) > MIN_DISTANCE && this.getVel() > MIN_SPEED) {
            this.finish();
        }
    }

    private void createVelocityTracker(MotionEvent event) {
        if (null == this.velocityTracker) {
            this.velocityTracker = VelocityTracker.obtain();
        }
        this.velocityTracker.clear();
        this.velocityTracker.addMovement(event);
    }

    private void recycleVelocityTracker() {
        this.velocityTracker.recycle();
        this.velocityTracker = null;
    }

    private float getVel() {
        if (null != this.velocityTracker) {
            this.velocityTracker.computeCurrentVelocity(1000);
            return this.velocityTracker.getXVelocity();
        }
        return -1;
    }

    /*
    * <T extends View> 规范参数
    * T 返回值
    * */
    protected <T extends View> T $(int id) {
        return (T) this.findViewById(id);
    }
}
