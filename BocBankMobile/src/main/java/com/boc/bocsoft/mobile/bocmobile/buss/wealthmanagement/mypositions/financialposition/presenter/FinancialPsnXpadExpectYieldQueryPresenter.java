package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.presenter;

import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadExpectYieldQuery.PsnXpadExpectYieldQueryParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadExpectYieldQuery.PsnXpadExpectYieldQueryResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadExpectYieldQueryOutlay.PsnXpadExpectYieldQueryOutlayParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadExpectYieldQueryOutlay.PsnXpadExpectYieldQueryOutlayResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.service.WealthManagementService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.util.FinancialPositionCodeModeUtil;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

/**
 * Created by Administrator on 2016/11/10.
 */
public class FinancialPsnXpadExpectYieldQueryPresenter  extends RxPresenter implements
        FinancialPositionContract.FinancialPsnXpadExpectYieldQueryPresenter{


    /**
     * 中银理财-service
     */
    private WealthManagementService mWealthManagementService;
    /**
     * 公共service
     */
    private GlobalService globalService;

    /**
     * 预期年华收益率---接口回调
     */
    private FinancialPositionContract.FinancialPsnXpadExpectYieldQueryView mExpectYieldQueryView;
    /***
     * 持仓列表-model 转换工具类
     */
    private FinancialPositionCodeModeUtil mFinancialPositionCodeModeUtil;

    /**
     * 构造器，做初始化
     * @param mExpectYieldQueryView
     */
    public FinancialPsnXpadExpectYieldQueryPresenter(FinancialPositionContract.FinancialPsnXpadExpectYieldQueryView mExpectYieldQueryView) {
        this.mExpectYieldQueryView = mExpectYieldQueryView;
        mWealthManagementService = new WealthManagementService();
        globalService = new GlobalService();
    }



    /**
     * 4.72 072业绩基准产品预计年收益率查询 PsnXpadExpectYieldQuery
     * productCode	产品代码
       queryDate	查询日期
     */
    @Override
    public void getPsnXpadExpectYieldQuery(String productCode,String queryDate) {
        PsnXpadExpectYieldQueryParams params = new PsnXpadExpectYieldQueryParams();
        params.setProductCode(productCode);
        params.setQueryDate(queryDate);
        mWealthManagementService.PsnXpadExpectYieldQuery(params)
                .compose(this.<PsnXpadExpectYieldQueryResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnXpadExpectYieldQueryResult>applyComputationSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnXpadExpectYieldQueryResult>(){
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onNext(PsnXpadExpectYieldQueryResult psnXpadExpectYieldQueryResult) {
                        mExpectYieldQueryView.obtainPsnXpadExpectYieldQuerySuccess(
                                mFinancialPositionCodeModeUtil.
                                        transverterPsnXpadExpectYieldQuery(psnXpadExpectYieldQueryResult));
                    }
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mExpectYieldQueryView.obtainPsnXpadExpectYieldQueryFault();
                    }
                    @Override
                    public void commonHandleException(BiiResultErrorException biiResultErrorException) {
                        super.commonHandleException(biiResultErrorException);
                    }
                });
    }


    /**
     * 4.73 073登录前业绩基准产品预计年收益率查询 PsnXpadExpectYieldQueryOutlay
     * productCode	产品代码
     queryDate	查询日期
     */
    @Override
    public void getPsnXpadExpectYieldQueryOutlay(String productCode, String queryDate) {
        PsnXpadExpectYieldQueryOutlayParams params = new PsnXpadExpectYieldQueryOutlayParams();
        params.setProductCode(productCode);
        params.setQueryDate(queryDate);
        mWealthManagementService.PsnXpadExpectYieldQueryOutlay(params)
                .compose(this.<PsnXpadExpectYieldQueryOutlayResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnXpadExpectYieldQueryOutlayResult>applyComputationSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnXpadExpectYieldQueryOutlayResult>(){
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onNext(PsnXpadExpectYieldQueryOutlayResult psnXpadExpectYieldQueryOutlayResult) {
                        mExpectYieldQueryView.obtainPsnXpadExpectYieldQueryOutlaySuccess(
                                mFinancialPositionCodeModeUtil.
                                        transverterPsnXpadExpectYieldQueryOutlay(psnXpadExpectYieldQueryOutlayResult));
                    }
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mExpectYieldQueryView.obtainPsnXpadExpectYieldQueryOutlayFault();
                    }
                    @Override
                    public void commonHandleException(BiiResultErrorException biiResultErrorException) {
                        super.commonHandleException(biiResultErrorException);
                    }
                });

    }
}
