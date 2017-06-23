package com.boc.bocsoft.mobile.bocmobile.buss.activitymanagementpaltform.presenter;

import com.boc.bocsoft.mobile.bii.bus.activitymanagementpaltform.model.psnActivityInfoQuery.PsnActivityInfoQueryParams;
import com.boc.bocsoft.mobile.bii.bus.activitymanagementpaltform.model.psnActivityInfoQuery.PsnActivityInfoQueryResult;
import com.boc.bocsoft.mobile.bii.bus.activitymanagementpaltform.service.ActivityManagementPaltformService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.activity.web.presenter.WebPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.activitymanagementpaltform.model.psnActivityInfoQuery.PsnActivityInfoQueryResModel;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;

import rx.functions.Func1;

/**
 * 微信 抽奖活动-首页-活动管理平台
 * Created by yx on 2016/12/20.
 */

public class ActivityManagementPaltformHomePagePresenter extends WebPresenter implements ActivityManagementPaltformHomePageContract.WeChatLuckDrawPresenter {

    private ActivityManagementPaltformHomePageContract.WeChatLuckDrawView mWeChatLuckDrawView;
    private ActivityManagementPaltformService mActivityManagementPaltformService;

    public ActivityManagementPaltformHomePagePresenter(ActivityManagementPaltformHomePageContract.WeChatLuckDrawView view) {
        super(view);
        mWeChatLuckDrawView = view;
        mActivityManagementPaltformService = new ActivityManagementPaltformService();
    }

    @Override
    public void getPsnActivityInfoQuery() {
        mActivityManagementPaltformService.psnActivityInfoQuery(new PsnActivityInfoQueryParams())
                .compose(this.<PsnActivityInfoQueryResult>bindToLifecycle())
                .map(new Func1<PsnActivityInfoQueryResult, PsnActivityInfoQueryResModel>() {
                    @Override
                    public PsnActivityInfoQueryResModel call(PsnActivityInfoQueryResult result) {//bii model 转换为工程使用model
                        PsnActivityInfoQueryResModel bean = new PsnActivityInfoQueryResModel();
                        bean.setCifNumber(result.getCifNumber());
                        bean.setCustomerId(result.getCustomerId());
                        bean.setOwnerBankId(result.getOwnerBankId());
                        bean.setOwnerIbkNum(result.getOwnerIbkNum());
                        bean.setTicketInfo(result.getTicketInfo());
                        bean.setActyUrl(result.getActyUrl());
                        return bean;
                    }
                })
                .compose(SchedulersCompat.<PsnActivityInfoQueryResModel>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnActivityInfoQueryResModel>() {

                    @Override
                    public void commonHandleException(
                            BiiResultErrorException biiResultErrorException) {
                    }

                    @Override
                    public void handleException(//接口失败调用
                                                BiiResultErrorException biiResultErrorException) {
                        mWeChatLuckDrawView.onPsnActivityInfoQueryFailed(
                                biiResultErrorException.getErrorMessage());
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnActivityInfoQueryResModel psnActivityInfoQueryResModel) {//接口成功调用处理
                        mWeChatLuckDrawView.onPsnActivityInfoQuerySuccess(psnActivityInfoQueryResModel);
                    }
                });
    }
}
