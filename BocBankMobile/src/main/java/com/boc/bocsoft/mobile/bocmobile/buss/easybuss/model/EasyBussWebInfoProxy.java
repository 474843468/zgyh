package com.boc.bocsoft.mobile.bocmobile.buss.easybuss.model;

import android.webkit.JavascriptInterface;
import com.boc.bocsoft.mobile.bocmobile.base.activity.web.model.WebInfoProxy;

/**
 * 作者：XieDu
 * 创建时间：2016/10/31 16:25
 * 描述：
 */
public class EasyBussWebInfoProxy extends WebInfoProxy<RedirectEzucBean> {

    @JavascriptInterface
    public String getLoginParams() {
        return getInfoJson();
    }
}
