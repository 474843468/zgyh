package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.presenter;

import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.service.WealthManagementService;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.util.FinancialPositionCodeModeUtil;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

/**
 * 业绩基准 图表显示 Presenter
 * Created by zn on 2016/10/9.
 */
public class FinancialTypeOutStandDetaileQueryPresenter extends RxPresenter
        implements FinancialPositionContract.FinancialTypeOutStandQueryPresenter {
    /**
     * 中银理财-service
     */
    private WealthManagementService mWealthManagementService;
    /**
     * 公共service
     */
    private GlobalService globalService;

    /**
     * 收益汇总查询-接口回调
     */
    private FinancialPositionContract.FinancialTypeOutStandQueryView mOutStandDetailQueryView;
    /***
     * 持仓列表-model 转换工具类
     */
    private FinancialPositionCodeModeUtil mFinancialPositionCodeModeUtil;

    /***
     * 构造器，做初始化
     *
     * @param mOutStandDetailQueryView
     */
    public FinancialTypeOutStandDetaileQueryPresenter(FinancialPositionContract.FinancialTypeOutStandQueryView mOutStandDetailQueryView) {
        this.mOutStandDetailQueryView = mOutStandDetailQueryView;
        mWealthManagementService = new WealthManagementService();
        globalService = new GlobalService();
    }

//    /**
//     * 收益详情列表查询
//     */
//    @Override
//    public void getPsnXpadReferProfitDetailQuery(PsnXpadReferProfitDetailQueryParams params) {
//        mWealthManagementService.psnXpadReferProfitDetailQuery(params)
//                .compose(this.<PsnXpadReferProfitDetailQueryResult>bindToLifecycle())
//                .compose(SchedulersCompat.<PsnXpadReferProfitDetailQueryResult>applyComputationSchedulers())
//                .subscribe(new BIIBaseSubscriber<PsnXpadReferProfitDetailQueryResult>() {
//                    @Override
//                    public void handleException(BiiResultErrorException biiResultErrorException) {
//                        mOutStandDetailQueryView.obtainPsnXpadReferProfitDetailQueryFault();
//                    }
//
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onNext(PsnXpadReferProfitDetailQueryResult psnXpadReferProfitDetailQueryResult) {
//                        mOutStandDetailQueryView.obtainPsnXpadReferProfitDetailQuerySuccess(
//                                mFinancialPositionCodeModeUtil.transverterPsnXpadReferProfitDetailQuery(
//                                        psnXpadReferProfitDetailQueryResult));
//                    }
//
//                    @Override
//                    public void commonHandleException(BiiResultErrorException biiResultErrorException) {
//                        super.commonHandleException(biiResultErrorException);
//                    }
//                });
//    }

}
