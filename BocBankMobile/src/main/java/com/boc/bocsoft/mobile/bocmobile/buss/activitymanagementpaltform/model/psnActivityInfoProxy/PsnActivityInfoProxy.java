package com.boc.bocsoft.mobile.bocmobile.buss.activitymanagementpaltform.model.psnActivityInfoProxy;

import android.webkit.JavascriptInterface;

import com.boc.bocsoft.mobile.bocmobile.base.activity.web.model.WebInfoProxy;
import com.boc.bocsoft.mobile.bocmobile.buss.activitymanagementpaltform.model.cabiienteracty.CABIIEnterActyReqModel;

/**
 * 作者：yx
 * 创建时间：2016-12-20 10:46:35
 * 描述：
 */
public class PsnActivityInfoProxy extends WebInfoProxy<CABIIEnterActyReqModel> {

    @JavascriptInterface
    public String getInfoJsonParams() {
        return getInfoJson();
    }//此处需要跟 活动管理平台对接 名字需要协商
}
