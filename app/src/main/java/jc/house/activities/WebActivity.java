package jc.house.activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.app.Activity;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import jc.house.R;
import jc.house.utils.ToastUtils;

public class WebActivity extends Activity {
    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_web);
        this.webView = (WebView)this.findViewById(R.id.webView);
        this.webView.loadUrl("http://mp.weixin.qq.com/s?__biz=MjM5ODQ5NDU0NQ==&mid=401086800&idx=1&sn=32714399b9af548a9fbe252fc9a76b33&scene=0#wechat_redirect");
        this.webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                ToastUtils.debugShow(WebActivity.this, "web load started");
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                ToastUtils.debugShow(WebActivity.this, "web load finished");
                super.onPageFinished(view, url);
            }
        });
    }

}
