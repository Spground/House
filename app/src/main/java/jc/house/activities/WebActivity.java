package jc.house.activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.app.Activity;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import jc.house.R;

public class WebActivity extends Activity {
    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_web);
        this.webView = (WebView)this.findViewById(R.id.webView);
        this.webView.loadUrl("http://kb.qq.com/");
        this.webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Toast.makeText(WebActivity.this, "web load started", Toast.LENGTH_SHORT).show();
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                Toast.makeText(WebActivity.this, "web load finished", Toast.LENGTH_SHORT).show();
                super.onPageFinished(view, url);
            }
        });
    }

}
