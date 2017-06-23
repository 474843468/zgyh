package com.boc.bocsoft.mobile.bocmobile.base.activity.web.ui;

import android.net.http.SslError;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConfig;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.RxBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.activity.web.model.WebInfoProxy;
import com.boc.bocsoft.mobile.bocmobile.base.activity.web.model.WebListener;
import com.boc.bocsoft.mobile.bocmobile.base.activity.web.presenter.WebPresenter;
import com.boc.bocsoft.mobile.bocmobile.base.widget.webView.BaseH5WebView;
import com.boc.bocsoft.mobile.common.utils.gson.GsonUtils;
import com.boc.bocsoft.mobile.framework.utils.NetworkUtils;

/**
 *
 */
public abstract class WebFragment<T, P extends WebPresenter> extends RxBussFragment<P>
        implements WebContract.View, WebListener {

    protected BaseH5WebView<WebInfoProxy> mWebView;
    private View rootView;
    protected WebInfoProxy<T> mWebInfoProxy;

    /**
     * 初始化布局
     */
    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_web, null);
        return rootView;
    }

    @Override
    public void initView() {
        mWebView = (BaseH5WebView) rootView.findViewById(R.id.webview);
        if (needCookie()) {
            setCookie();
        }
        setWebView();
    }

    /**
     * 如果需要设置webview的cookie，打开这一项
     */
    private boolean needCookie() {
        return true;
    }

    private void setCookie() {
        NetworkUtils.setWebViewCookie(mWebView.getWebView(), createCookieUrl());
    }

    @Override
    public void initData() {
        mWebInfoProxy = createWebInfoProxy();
        mWebInfoProxy.setWebListener(this);
        if (openHeartBeatWhenInitData() && getPresenter() != null) {
            getPresenter().qryHeartBeat(10);
        }
    }

    protected abstract WebInfoProxy<T> createWebInfoProxy();

    protected void setWebView() {
        setWebSettings();
        if (showWebViewProgress()) {
            mWebView.getWebView().setWebChromeClient(new WebChromeClient() {
                @Override
                public void onProgressChanged(WebView view, int newProgress) {
                    if (newProgress == 100) {
                        closeProgressDialog();
                    }
                }
            });
        }
        //TODO 测试时打开这段，发布正式版本时要注释掉
        //igNoreSslError();
    }

    private void igNoreSslError() {
        mWebView.getWebView().setWebViewClient(new BaseH5WebView.BaseH5WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }
        });
    }

    protected void setWebSettings() {
    }

    @Override
    public boolean onBack() {
        if (mWebView.getWebView().canGoBack()) {
            mWebView.getWebView().goBack();
        } else {
            onClosed();
        }
        return false;
    }

    @Override
    protected void titleLeftIconClick() {
        onClosed();
    }

    /**
     * 是否需要显示加载webview时的进度条，默认显示
     */
    protected boolean showWebViewProgress() {
        return true;
    }

    /**
     * 是否开启心跳包连接监测选项,默认开启
     */
    protected boolean openHeartBeatWhenInitData() {
        return true;
    }

    private String createCookieUrl() {
        String biiUrl = ApplicationConfig.BII_URL;
        if (!biiUrl.endsWith("/")) {
            biiUrl += "/";
        }
        return biiUrl;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeSessionCookie();
        cookieManager.removeAllCookie();
    }

    /**
     * 原生通过调用js方法的方式，将信息传输给js
     *
     * @param info 被传输的信息
     * @param jsMethod js的方法名
     */
    public void postInfoToJs(Object info, String jsMethod) {
        mWebView.loadUrl("javascript:" + jsMethod + "(" + GsonUtils.getGson().toJson(info) + ")");
    }

    /**
     * 原生通过调用js方法的方式，将信息传输给js
     * 方法名默认为"receiveInfoFromMobile"
     *
     * @param info 被传输的信息
     */
    public void postInfoToJs(Object info) {
        postInfoToJs(info, "receiveInfoFromMobile");
    }
}