package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.presenter;

import com.boc.bocsoft.mobile.bii.bus.global.model.PSNBmpsCreatConversation.PSNBmpsCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProgressQuery.PsnXpadProgressQueryParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProgressQuery.PsnXpadProgressQueryResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProgressQueryOutlay.PsnXpadProgressQueryOutlayParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProgressQueryOutlay.PsnXpadProgressQueryOutlayResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.service.WealthManagementService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.util.FinancialPositionCodeModeUtil;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import rx.Observable;
import rx.functions.Func1;

/**
 * 4.34 034累进产品收益率查询PsnXpadProgressQuery
 * Created by cff on 2016/10/19.
 */
public class FinancialTypeProgressQueryPresenter extends RxPresenter
        implements FinancialPositionContract.FinancialTypeProgressQueryPresenter{

    /**
     * 中银理财-service
     */
    private WealthManagementService mWealthManagementService;
    /**
     * 公共service
     */
    private GlobalService globalService;

    /**
     * 收益率列表-接口回调
     */
    private FinancialPositionContract.FinancialTypeProgressQueryView mFinancialProgressView;
    /***
     * 持仓列表-model 转换工具类
     */
    private FinancialPositionCodeModeUtil mFinancialPositionCodeModeUtil;

    /**
     * 构造器，做初始化
     *
     * @param mFinancialProgressView
     */
    public FinancialTypeProgressQueryPresenter(FinancialPositionContract.FinancialTypeProgressQueryView mFinancialProgressView) {
        this.mFinancialProgressView = mFinancialProgressView;
        mWealthManagementService = new WealthManagementService();
        globalService = new GlobalService();
    }


    /**
     * 获取会话
     */
    @Override
    public void getPSNCreatConversation() {
        PSNCreatConversationParams mParams = new PSNCreatConversationParams();
        globalService.psnCreatConversation(mParams)
                .compose(this.<String>bindToLifecycle())
                .compose(SchedulersCompat.<String>applyComputationSchedulers())
                .subscribe(new BIIBaseSubscriber<String>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(String s) {
                        mFinancialProgressView.obtainConversationSuccess(s);
                    }

                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mFinancialProgressView.obtainConversationFail();
                    }
                });
    }


    /**
     * 已登录 -- 收益累进产品--年华收益率查询
     * @param params
     */
    @Override
    public void getPsnXpadProgressQuery(PsnXpadProgressQueryParams params) {
        mWealthManagementService.PsnXpadProgressQuery(params)
                .compose(this.<PsnXpadProgressQueryResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnXpadProgressQueryResult>applyComputationSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnXpadProgressQueryResult>(){
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onNext(PsnXpadProgressQueryResult psnXpadProgressQueryResult) {
                        mFinancialProgressView.obtainPsnXpadProgressQuerySuccess(
                                mFinancialPositionCodeModeUtil.transverterPsnXpadProgressQuery(psnXpadProgressQueryResult));
                    }
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mFinancialProgressView.obtainPsnXpadProgressQueryFault();
                    }
                    @Override
                    public void commonHandleException(BiiResultErrorException biiResultErrorException) {
                        super.commonHandleException(biiResultErrorException);
                    }
                });
    }

    /**
     * 未登录 -- 收益累进产品--年华收益率查询
     * 4.53 053 PsnXpadProgressQueryOutlay 登录前累进产品收益率查询
     *   @param OutlayParams
     */
    @Override
    public void getPsnXpadProgressQueryOutlay(final PsnXpadProgressQueryOutlayParams OutlayParams) {
        PSNBmpsCreatConversationParams params = new PSNBmpsCreatConversationParams();
        globalService.psnBmpsCreatConversation(params)
                .compose(this.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnXpadProgressQueryOutlayResult>>() {
                    @Override
                    public Observable<PsnXpadProgressQueryOutlayResult> call(String s) {
                        OutlayParams.setConversationId(s);
                        return mWealthManagementService.PsnXpadProgressQueryOutlay(OutlayParams);
                    }
                })
                .compose(SchedulersCompat.<PsnXpadProgressQueryOutlayResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnXpadProgressQueryOutlayResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mFinancialProgressView.obtainPsnXpadProgressQueryOutlayFault();
                    }
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onNext(PsnXpadProgressQueryOutlayResult result) {
                        mFinancialProgressView.obtainPsnXpadProgressQueryOutlaySuccess(
                                mFinancialPositionCodeModeUtil.transverterPsnXpadProgressQueryOutlay(result));
                    }
                });
    }
}
