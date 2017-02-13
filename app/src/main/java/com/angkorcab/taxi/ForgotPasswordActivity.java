package com.angkorcab.taxi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.angkorcab.constants.ProjectURls;
import com.angkorcab.helper.MyWebViewClient;

public class ForgotPasswordActivity extends AppCompatActivity {
    WebView forgot_WebView;
    private Handler mHandler = new Handler();
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        try {
            context = ForgotPasswordActivity.this;
            forgot_WebView = (WebView) findViewById(R.id.forgot_password_webview);
            MyWebViewClient.enableWebViewSettings(forgot_WebView);
            forgot_WebView.setWebChromeClient(new MyWebChromeClient());
            forgot_WebView.setWebViewClient(new MyWebViewClient(this));
            forgot_WebView.addJavascriptInterface(new DemoJavaScriptInterface(),
                    "android");
            forgot_WebView.loadUrl(ProjectURls.FORGOT_PASSWORD_URL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    final class DemoJavaScriptInterface {
        DemoJavaScriptInterface() {
        }

        @JavascriptInterface
        public void clickOnAndroidLogin() {
            mHandler.post(new Runnable() {
                public void run() {
                    startActivity(new Intent(ForgotPasswordActivity.this,
                            LoginActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                }
            });
        }
    }

    final class MyWebChromeClient extends WebChromeClient {
        @Override
        public boolean onJsAlert(WebView view, String url, String message,
                                 JsResult result) {
            result.confirm();
            return true;
        }
    }
}
