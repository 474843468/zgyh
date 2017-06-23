package com.boc.bocsoft.mobile.bocmobile.base.widget.webView;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.boc.bocsoft.mobile.common.utils.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Set;

/**
 * 作者：XieDu
 * 创建时间：2016/9/13 15:47
 * 描述：基本H5WebView
 * 为了防止返回时遇到重定向无法返回，shouldOverrideUrlLoading方法返回false
 */
public class BaseH5WebView<T extends IWebInfoProxy> extends LinearLayout {
    /**
     * key,用于H5页面识别
     */
    private String mKey;

    /**
     * 合同传递对象
     */
    private T mWebInfoProxy;

    /*上下文*/
    public Context mContext;
    /**
     * WebView
     */
    public WebView mWebView;
    /**
     * url
     */
    public String defaultLoadUrl;

    public BaseH5WebView(Context context) {
        this(context, null);
    }

    public BaseH5WebView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseH5WebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
        initData();
    }

    protected void initData() {
    }

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
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setJavaScriptEnabled(false);
        webSettings.setUseWideViewPort(false);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setSaveFormData(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            webSettings.setAllowFileAccessFromFileURLs(false);
        }
        webSettings.setDomStorageEnabled(false);
        mWebView.setVerticalScrollBarEnabled(false);
    }

    protected void setWebView() {
        mWebView.setWebViewClient(new BaseH5WebViewClient());
    }

    /**
     * 传值对象
     */
    public void setInfoProxy(T infoProxy) {
        mWebInfoProxy = infoProxy;
    }

    public T getInfoProxy() {
        return mWebInfoProxy;
    }

    public void setKey(String key) {
        mKey = key;
    }

    public void loadUrl(String url) {
        if (!StringUtils.isEmpty(url)) {
            mWebView.loadUrl(url);
        }
        if (mWebInfoProxy != null) {
            mWebView.addJavascriptInterface(mWebInfoProxy, mKey);
        }
    }

    /**
     * 给h5 传递参数 拼接字符串
     *
     * @param url
     * @param info
     * @date 2016-12-28 11:04:18
     * @author 谢端阳
     */
    public void postUrl(String url, String info) {
        if (!StringUtils.isEmpty(url)) {
            try {
                mWebView.postUrl(url, URLEncoder.encode(info, "utf-8").getBytes());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 给h5 传递参数 拼接字符串
     *
     * @param url
     * @param maps
     * @date 2016-12-28 11:04:18
     * @author 陈若煜
     */
    public void postUrlForm(String url, Map<String, String> maps) {
        String info;
        if (maps == null || maps.size() == 0)
            info = "";
        else {
            StringBuilder sBuilder = new StringBuilder();
            Set<String> keys = maps.keySet();
//            Set<Map.Entry<String,String>> keys = maps.entrySet();
            String value;
            for (String key : keys) {
                value = maps.get(key);
                if (StringUtils.isEmptyOrNull(value)) continue;
                sBuilder.append("&");
                sBuilder.append(key).append("=").append(value);
            }
            info = sBuilder.substring(1);
        }
        if (!StringUtils.isEmpty(url)) {
            try {
                if (info == null) info = "";
//                mWebView.postUrl(url, URLEncoder.encode(info, "utf-8").getBytes());
                mWebView.postUrl(url, info.getBytes("utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 传递url处理
     */
    public void setDefaultLoadUrl(String url) {
        if (!TextUtils.isEmpty(url)) {
            defaultLoadUrl = url;
        }
    }

    public void loadDefaultUrl() {
        loadUrl(defaultLoadUrl);
    }

    public WebView getWebView() {
        return mWebView;
    }

    /**
     * 为了防止返回时遇到重定向无法返回，shouldOverrideUrlLoading方法返回false
     */
    public static class BaseH5WebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return false;
        }
    }
}
