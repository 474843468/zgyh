package com.boc.bocsoft.mobile.bocmobile.base.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.framework.utils.NetworkUtils;

/**
 * 基础的webview activity
 * Created by lxw on 2016/7/28 0028.
 */
public class WebViewActivity extends BaseMobileActivity{

    public static final String URL = "url";
    public static final String TITLE = "title";

    private static final String URL_PREFIX_HTTP = "http";
    private static final String URL_PREFIX_HTTPS = "https";
    private static final String URL_PREFIX_FILE = "file";

    private String mUrl;

    protected Handler mHandler = new Handler();
    protected WebView mWebView;
    protected LinearLayout llNetError;

    protected ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected View getContentView() {
        View root = View.inflate(mContext, R.layout.boc_activity_webview, null);
        mWebView = (WebView) root.findViewById(R.id.webview);
        llNetError = (LinearLayout)root.findViewById(R.id.llNetError);
        configWebView(mWebView);
        progressBar = (ProgressBar) root.findViewById(R.id.progressBar);
        return root;
    }

    @Override
    public void initView() {
        mTitleBarView.setRightImgBtnVisible(false);
        super.initView();
        mUrl = getIntent().getStringExtra(URL);
        String title = getIntent().getStringExtra(TITLE);
        this.mTitleBarView.setTitle(title);

        llNetError.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                loadRemoteUrl();
            }
        });
        loadRemoteUrl();
    }

    /**
     * 设置webview
     * @param webView
     */
    private void configWebView(WebView webView){

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            /**
             * 加载资源
             * @param view
             * @param url
             */
            @Override
            public void onLoadResource (WebView view, String url) {

            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                showLoadingDialog();
            }

            /**
             * 页面加载完成
             * @param view
             * @param url
             */
            public void onPageFinished(WebView view, String url) {
                closeProgressDialog();
            }

            /**
             * 加载页面发生错误
             * @param view
             * @param errorCode
             * @param description
             * @param failingUrl
             */
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
//                mWebView.stopLoading();
//                mWebView.clearView();
//                mHandler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        mWebView.setVisibility(View.GONE);
//                        llNetError.setVisibility(View.VISIBLE);
//                    }
//                });

            }
        });

        webView.getSettings().setDomStorageEnabled(true);
        WebSettings webSettings = webView.getSettings();
        webSettings.setDisplayZoomControls(false);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.getSettings().setSupportZoom(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setSaveFormData(false);
        webSettings.setDomStorageEnabled(true);

        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
//                if (progressBar != null) {
//                    if (newProgress == 100) {
//                        progressBar.setVisibility(View.GONE);
//                    } else {
//                        progressBar.setVisibility(View.VISIBLE);
//                        progressBar.setProgress(newProgress);
//                    }
//
//                }


            }
        });
        webView.setVerticalScrollBarEnabled(false);

    }

    @Override
    protected void onPause() {
        super.onPause();
        // Flash video may keep playing if the webView isn't paused here
        pauseWebView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        resumeWebView();
    }

    private void pauseWebView() {
        if (mWebView != null) {
            mWebView.onPause();
        }
    }

    private void resumeWebView() {
        if (mWebView != null) {
            mWebView.onResume();
        }
    }

    /**
     * Load the specified URL in the webview.
     *
     * @param url URL to load in the webview.
     */
    protected void loadUrl(String url) {
        mWebView.loadUrl(url);
        onLoad();
    }

    /**
     * 之类实现该方法，提交js调用接口
     */
    protected void onLoad(){

    }

    @Override
    // Detect when the back button is pressed
    public void onBackPressed() {
        if(mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            // Let the system handle the back button
            super.onBackPressed();
        }
    }

    /**
     * 红色主题titleBar：0 ；
     * 白色主题titleBar：1 ；
     * @return
     */
    protected Boolean getTitleBarRed(){
        return false;
    }

    /**
     * 加载远端的url地址
     */
    private void loadRemoteUrl(){

        if (!StringUtils.isEmptyOrNull(mUrl)){

            if(mUrl.startsWith(URL_PREFIX_FILE)){
                mWebView.setVisibility(View.VISIBLE);
                llNetError.setVisibility(View.GONE);
                loadUrl(mUrl);
            } else if(mUrl.startsWith(URL_PREFIX_HTTP) || mUrl.startsWith(URL_PREFIX_HTTPS)){
                if(!NetworkUtils.haveNetworkConnection(this)){
                    mWebView.setVisibility(View.GONE);
                    llNetError.setVisibility(View.VISIBLE);
                } else {
                    mWebView.setVisibility(View.VISIBLE);
                    llNetError.setVisibility(View.GONE);
                    loadUrl(mUrl);
                }
            }
        } else {
            Toast.makeText(mContext, "URL地址不能为空", Toast.LENGTH_SHORT).show();
        }
    }
}
