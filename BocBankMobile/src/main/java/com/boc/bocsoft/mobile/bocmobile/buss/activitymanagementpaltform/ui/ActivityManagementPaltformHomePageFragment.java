package com.boc.bocsoft.mobile.bocmobile.buss.activitymanagementpaltform.ui;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.web.model.WebInfoProxy;
import com.boc.bocsoft.mobile.bocmobile.base.activity.web.ui.WebFragment;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.ErrorDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.userwidget.echarsview.GsonTools;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.ModuleActivityDispatcher;
import com.boc.bocsoft.mobile.bocmobile.buss.activitymanagementpaltform.model.cabiienteracty.CABIIEnterActyReqModel;
import com.boc.bocsoft.mobile.bocmobile.buss.activitymanagementpaltform.model.psnActivityInfoProxy.PsnActivityInfoProxy;
import com.boc.bocsoft.mobile.bocmobile.buss.activitymanagementpaltform.model.psnActivityInfoQuery.PsnActivityInfoQueryResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.activitymanagementpaltform.presenter.ActivityManagementPaltformHomePageContract;
import com.boc.bocsoft.mobile.bocmobile.buss.activitymanagementpaltform.presenter.ActivityManagementPaltformHomePagePresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.ui.WealthProductFragment;
import com.boc.bocsoft.mobile.common.utils.gson.GsonUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 微信抽奖活动-首页-活动管理平台
 * <p>
 * Created by yx on 2016/12/20.
 */

public class ActivityManagementPaltformHomePageFragment extends WebFragment<CABIIEnterActyReqModel, ActivityManagementPaltformHomePagePresenter>
        implements ActivityManagementPaltformHomePageContract.WeChatLuckDrawView {
    private String mWebUrl = "http://22.11.88.63/CustActyApp/CACBIIEnterActy.do";//调试使用

    @Override
    protected void setWebView() {
        super.setWebView();
        mWebView.setKey("app_wcld");//需要跟活动管理平台约定 key名称
    }

    @Override
    public void initData() {
        super.initData();
        showLoadingDialog();
        getPresenter().getPsnActivityInfoQuery();
    }

    @Override
    protected WebInfoProxy<CABIIEnterActyReqModel> createWebInfoProxy() {
        return new PsnActivityInfoProxy();
    }

    @Override
    protected ActivityManagementPaltformHomePagePresenter initPresenter() {
        return new ActivityManagementPaltformHomePagePresenter(this);
    }


    /**
     * 标题栏右侧图标点击事件
     */
    @Override
    protected void titleRightIconClick() {
        super.titleRightIconClick();
    }

    /**
     * 是否显示右侧标题按钮
     */
    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }

    /**
     * 是否显示红头
     *
     * @return
     */
    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    /**
     * 设置标题
     */
    @Override
    protected String getTitleValue() {
        return "热门活动";
    }

    @Override
    protected boolean isHaveTitleBarView() {
        return true;
    }

    @Override
    protected void titleLeftIconClick() {
//        ModuleActivityDispatcher.popToHomePage();
        super.titleLeftIconClick();
    }

    @Override
    public void onClosed() {
        ModuleActivityDispatcher.popToHomePage();
    }

    @SuppressWarnings("deprecation")
//    @Override
//    protected void setWebSettings() {//暂定关闭 用默认
//        mWebView.clearDefaultSetting();
//        WebSettings webSettings = mWebView.getWebView().getSettings();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//            webSettings.setAllowFileAccessFromFileURLs(true);
//            webSettings.setAllowUniversalAccessFromFileURLs(true);
//        }
//        webSettings.setAppCacheMaxSize(1024 * 1024 * 8);
//        webSettings.setJavaScriptEnabled(true);// 用于JS回调
//        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);// 提高渲染的优先级
//        webSettings.setDomStorageEnabled(true);
//        webSettings.setAllowFileAccess(true);
//        webSettings.setGeolocationEnabled(true);
//        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
//        webSettings.setDefaultTextEncodingName("UTF-8");
//    }


    @Override
    public void onPsnActivityInfoQuerySuccess(PsnActivityInfoQueryResModel psnActivityInfoQueryResModel) {
        mWebView.getWebView().clearCache(true);
        mWebView.setInfoProxy(mWebInfoProxy);
        Map<String, String> maps = new HashMap<>();
        maps.put("channel", "2");
        maps.put("tokenCode", psnActivityInfoQueryResModel.getTicketInfo() + "");
        maps.put("ticketMsg", psnActivityInfoQueryResModel.getCustomerId() + "|"
                + psnActivityInfoQueryResModel.getCifNumber() + "|"
                + "2" + "|" + psnActivityInfoQueryResModel.getOwnerIbkNum());
        mWebUrl = psnActivityInfoQueryResModel.getActyUrl();
        mWebView.postUrlForm(mWebUrl, maps);
    }

    @Override
    public void onPsnActivityInfoQueryFailed(String errorMessage) {
        closeProgressDialog();
        ErrorDialog errorDialog =
                new ErrorDialog(mContext).setBtnText("确认").setErrorData(errorMessage);
        errorDialog.setOnBottomViewClickListener(new ErrorDialog.OnBottomViewClickListener() {
            @Override
            public void onBottomViewClick() {
                ActivityManagementPaltformHomePageFragment.this.onClosed();
            }
        });
        errorDialog.show();
    }
}
