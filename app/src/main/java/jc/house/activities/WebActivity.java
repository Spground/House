package jc.house.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import jc.house.R;
import jc.house.global.Constants;
import jc.house.utils.LogUtils;
import jc.house.utils.StringUtils;

public class WebActivity extends BaseActivity {
    private WebView webView;
    public static final String FLAG_URL = "url";
    public static final String FLAG_TITLE = "title";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setJCContentView(R.layout.activity_web);
        this.progressDialog.show();
        this.webView = (WebView) this.findViewById(R.id.webView);
        this.webView.getSettings().setBlockNetworkImage(false);
        this.webView.getSettings().setBlockNetworkLoads(false);
        this.webView.getSettings().setJavaScriptEnabled(true);
        Intent intent = this.getIntent();
        String url = intent.getStringExtra(FLAG_URL);
        ToastL(url);
        String title = intent.getStringExtra(FLAG_TITLE);
        setTitleBarTitle(title);
        if (!StringUtils.strEmpty(url)) {
            this.webView.loadUrl(url);
        } else {
            this.webView.loadUrl(Constants.SERVER_URL + "news/mobile&id=12");
        }
        this.webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                LogUtils.debug("WebView", "web load started");
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                LogUtils.debug("WebView", "web load finished");
                super.onPageFinished(view, url);
                progressDialog.dismiss();
            }
        });
        this.setScrollRightBack(true);
    }

}
