package com.boc.bocsoft.mobile.bocmobile.buss.activitymanagementpaltform.model.psnActivityInfoProxy;

import android.webkit.JavascriptInterface;

import com.boc.bocsoft.mobile.bocmobile.base.activity.web.model.WebInfoProxy;
import com.boc.bocsoft.mobile.bocmobile.buss.activitymanagementpaltform.model.cabiienteracty.TransRemitCABIIEnterActyModel;

/**
 * Created by cry7096 on 2016/12/26.
 */
public class PsnTransActivityProxy extends WebInfoProxy<TransRemitCABIIEnterActyModel> {
    @JavascriptInterface
    public String getInfoJsonParams() {
        return getInfoJson();
    }//此处需要跟 活动管理平台对接 名字需要协商
}