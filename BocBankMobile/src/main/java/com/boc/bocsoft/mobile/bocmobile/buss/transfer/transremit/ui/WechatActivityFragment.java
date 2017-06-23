package com.boc.bocsoft.mobile.bocmobile.buss.transfer.transremit.ui;

import android.os.Bundle;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.web.model.WebInfoProxy;
import com.boc.bocsoft.mobile.bocmobile.base.activity.web.presenter.WebPresenter;
import com.boc.bocsoft.mobile.bocmobile.base.activity.web.ui.WebFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.activitymanagementpaltform.model.cabiienteracty.TransRemitCABIIEnterActyModel;
import com.boc.bocsoft.mobile.bocmobile.buss.activitymanagementpaltform.model.psnActivityInfoProxy.PsnTransActivityProxy;
import com.boc.bocsoft.mobile.common.utils.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by cry7096 on 2016/12/26.
 * 转账汇款结果页-参加活动-跳微信界面
 */
public class WechatActivityFragment extends WebFragment<TransRemitCABIIEnterActyModel, WebPresenter> {

    //传入的请求接口数据
    private TransRemitCABIIEnterActyModel wechatData;
    //活动的url地址
    private String actyUrl;

    public static WechatActivityFragment getInstance(String actyUrl, TransRemitCABIIEnterActyModel transRemitCABIIEnterActyModel) {
        Bundle bundle = new Bundle();
        bundle.putString("ACTYURL",actyUrl);
        bundle.putParcelable("WHECHAT", transRemitCABIIEnterActyModel);
        WechatActivityFragment fragment = new WechatActivityFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void initData() {
        super.initData();
        actyUrl = getArguments().getString("ACTYURL");
        wechatData = getArguments().getParcelable("WHECHAT");

        Map<String,String> maps = new HashMap<>();
        maps.put("channel",wechatData.getChannel());
        maps.put("tokenCode",wechatData.getTokenCode());
        maps.put("ticketMsg",wechatData.getTicketMsg());
        maps.put("actyId",wechatData.getActyId());

        //请求微信界面并post数据
//        mWebView.postUrl(actyUrl, buildParams(maps));
        mWebView.postUrlForm(actyUrl, maps);
     }

    /**
     * 将String转换为Form形式
     * @param maps
     * @return
     */
    private String buildParams(Map<String,String> maps){
        if(maps == null|| maps.size() == 0)return "";
        StringBuilder sBuilder = new StringBuilder();

        Set<String> keys = maps.keySet();
        String value;
        for(String key:keys){
            value = maps.get(key);
            if(StringUtils.isEmptyOrNull(value))continue;
            sBuilder.append("&");
            sBuilder.append(key).append("=").append(value);
        }
        return sBuilder.substring(1);
    }

    @Override
    protected WebPresenter initPresenter() {
        return null;
    }

    @Override
    public void onClosed() {
        pop();
    }

    @Override
    protected boolean isHaveTitleBarView() {
        return true;
    }

    @Override
    protected WebInfoProxy<TransRemitCABIIEnterActyModel> createWebInfoProxy() {
        return new PsnTransActivityProxy();
    }

    @Override
    protected void titleLeftIconClick() {
        super.titleLeftIconClick();
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }

    @Override
    protected String getTitleValue() {
        return getResources().getString(R.string.trans_activity_management_title);
    }
}
