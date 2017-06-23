package com.boc.bocsoft.mobile.bocmobile.base.widget.webView;

import android.webkit.JavascriptInterface;

/**
 * 作者：XieDu
 * 创建时间：2016/10/14 19:33
 * 描述：
 */
public interface IWebInfoProxy {

    /**
     * 让网页能获取信息
     * @return 信息的json字符串
     */
    @JavascriptInterface
    String getInfoJson();
    /**
     * 关闭页面的方法，供网页回调。根据是否有关闭需要来决定是否实现。
     */
    @JavascriptInterface
    void close();
}
