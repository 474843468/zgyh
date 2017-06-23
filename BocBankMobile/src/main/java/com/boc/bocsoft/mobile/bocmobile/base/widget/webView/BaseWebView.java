package com.boc.bocsoft.mobile.bocmobile.base.widget.webView;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

/**
 * Created by huixiaobo on 2016/7/25.
 * 公用webView
 */
public class BaseWebView extends LinearLayout {
    /*上下文*/
    public Context mContext;
    /** WebView */
    public WebView mWebView;
    /** url */
    public String defaultLoadUrl;

    public BaseWebView(Context context) {
        this(context, null, 0);
    }

    public BaseWebView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
        initData();
    }

    protected void initData() {}

    public void initView() {
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mWebView = new WebView(mContext);
        mWebView.setLayoutParams(params);
        addView(mWebView);
        defaultSetting();
        setWebView();
    }

    /**
     * 传递url处理
     */
    public void setDefaultLoadUrl(String url) {
        if (!TextUtils.isEmpty(url)) {
            defaultLoadUrl = url;
        }
    }

    /**
     * webView默认设置
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void defaultSetting() {
        if (isInEditMode()) {
            return;
        }
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setSaveFormData(false);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setDomStorageEnabled(true);
        mWebView.setVerticalScrollBarEnabled(false);
    }

    public void clearDefaultSetting() {
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setBuiltInZoomControls(false);
        webSettings.setDisplayZoomControls(true);
        webSettings.setCacheMode(WebSettings. LOAD_DEFAULT);
        webSettings.setJavaScriptEnabled(false);
        webSettings.setUseWideViewPort(false);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm. NARROW_COLUMNS);
        webSettings.setSaveFormData(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            webSettings.setAllowFileAccessFromFileURLs(false);
        }
        webSettings.setDomStorageEnabled(false);
        mWebView.setVerticalScrollBarEnabled(false);
    }

    protected void setWebView() {
        //webview 页面交互
        mWebView.getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        mWebView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        mWebView.setWebChromeClient(new SettingPasswordWebChromeClient());
                        fankInteractive();
                        mWebView.setWebViewClient(new WebViewClient() {
                            @Override
                            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                view.loadUrl(url);
                                return true;
                            }
                        });
                    }
                });
    }

    public final class SettingPasswordWebChromeClient extends WebChromeClient {
        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            return super.onJsAlert(view, url, message, result);
        }
    }

    /**
     * 后续方法处理
     */
    public void fankInteractive() {
        if (!TextUtils.isEmpty(defaultLoadUrl)) {
            mWebView.loadUrl(defaultLoadUrl);
        }
    }

    public WebView getWebView() {
        return mWebView;
    }
}
