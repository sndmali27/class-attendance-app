package com.school.result;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends Activity {
    WebView webView;
    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        webView = new WebView(this);
        setContentView(webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient());
        webView.addJavascriptInterface(new PrintBridge(this), "AndroidPrint");
        webView.loadUrl("file:///android_asset/index.html");
    }
    public class PrintBridge {
        Context context;
        PrintBridge(Context c) { context = c; }
        @JavascriptInterface public void printHtml(String html) {
            runOnUiThread(() -> {
                WebView printView = new WebView(MainActivity.this);
                printView.setWebViewClient(new WebViewClient() {
                    @Override public void onPageFinished(WebView view, String url) {
                        PrintManager pm = (PrintManager) getSystemService(Context.PRINT_SERVICE);
                        android.print.PrintDocumentAdapter adapter = view.createPrintDocumentAdapter("Class6-Result");
                        PrintAttributes attrs = new PrintAttributes.Builder()
                            .setMediaSize(PrintAttributes.MediaSize.ISO_A4)
                            .setMinMargins(new PrintAttributes.Margins(200, 200, 200, 200))
                            .build();
                        pm.print("Class6-Result", adapter, attrs);
                    }
                });
                printView.loadDataWithBaseURL("file:///android_asset/", html, "text/html", "UTF-8", null);
            });
        }
    }
    @Override public void onBackPressed() {
        if (webView != null && webView.canGoBack()) webView.goBack();
        else super.onBackPressed();
    }
}
