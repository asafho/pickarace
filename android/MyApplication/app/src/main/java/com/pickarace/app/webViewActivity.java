package com.pickarace.app;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.pickarace.app.pickarace.R;


public class webViewActivity  extends Activity {

    private WebView web_view;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);

        web_view = (WebView) findViewById(R.id.webview);
        web_view.getSettings().setJavaScriptEnabled(true);
        web_view.loadUrl(getIntent().getStringExtra("eventLink"));

        web_view.setVerticalScrollBarEnabled(true);
        web_view.setHorizontalScrollBarEnabled(true);

        web_view.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView viewx, String urlx) {
                viewx.loadUrl(urlx);
                return false;
            }
        });
    }

}