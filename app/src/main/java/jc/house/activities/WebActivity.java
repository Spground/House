package jc.house.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import jc.house.R;
import jc.house.utils.LogUtils;
import jc.house.utils.StringUtils;

public class WebActivity extends BaseActivity {
    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        setTitleBarTitle("Web界面");
        this.progressDialog.show();
        this.webView = (WebView)this.findViewById(R.id.webView);
        this.webView.getSettings().setBlockNetworkImage(false);
        this.webView.getSettings().setBlockNetworkLoads(false);
        this.webView.getSettings().setJavaScriptEnabled(true);
        Intent intent = this.getIntent();
        String url = intent.getStringExtra("url");
        if (!StringUtils.strEmpty(url)) {
            this.webView.loadUrl(url);
        } else {
            this.webView.loadUrl("http://zhan.qq.com/sites/templates/41428/index.html");
//            this.webView.loadUrl("http://192.168.9.72/house/web/index.php?r=news2%2Fmobile&id=12");
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

    }

}
