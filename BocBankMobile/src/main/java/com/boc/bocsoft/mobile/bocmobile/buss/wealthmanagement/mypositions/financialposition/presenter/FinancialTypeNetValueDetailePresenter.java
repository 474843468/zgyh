package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.presenter;

import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetTokenId.PSNGetTokenIdParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadReferProfitQuery.PsnXpadReferProfitQueryParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadReferProfitQuery.PsnXpadReferProfitQueryResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadSetBonusMode.PsnXpadSetBonusModeParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadSetBonusMode.PsnXpadSetBonusModeResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.service.WealthManagementService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.model.psnxpadproductbalancequery.PsnXpadProductBalanceQueryResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.util.FinancialPositionCodeModeUtil;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import rx.Observable;
import rx.functions.Func1;

/**
 * 中银理财-我的持仓-净值型-Presenter
 * Created by zn on 2016/9/22.
 */
public class FinancialTypeNetValueDetailePresenter extends RxPresenter
        implements FinancialPositionContract.FinancialTypeNetValuePresenter {
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
    private FinancialPositionContract.FinancialTypeNetValueView mNetValueDetailView;
    /***
     * 持仓列表-model 转换工具类
     */
    private FinancialPositionCodeModeUtil mFinancialPositionCodeModeUtil;

    /**
     * 构造器，做初始化
     *
     * @param mNetValueDetailView
     */
    public FinancialTypeNetValueDetailePresenter(FinancialPositionContract.FinancialTypeNetValueView mNetValueDetailView) {
        this.mNetValueDetailView = mNetValueDetailView;
        mWealthManagementService = new WealthManagementService();
        globalService = new GlobalService();
    }

    /**
     * 初始化
     */
    private void init() {

    }

    /**
     * PsnXpadReferProfitQuery  收益汇总查询
     *
     * @param accountKey  账号缓存标识(必选)
     * @param productCode 产品代码(必选)
     * @param kind        产品性质(必选)
     * @param charCode    钞汇标识(必输01：钞;02：汇;00：人民币)
     * @param tranSeq     份额流水号(业绩基准产品必输（PsnXpadProductBalanceQuery返回项standPro不为0的产品），
     *                    取自 PsnXpadQuantityDetail 接口返回项 tranSeq)
     */
    @Override
    public void getPsnXpadReferProfitQuery(String accountKey, String productCode,
                                           String kind, String charCode, String tranSeq) {
        PsnXpadReferProfitQueryParams params = new PsnXpadReferProfitQueryParams();
        params.setAccountKey(accountKey);
        params.setProductCode(productCode);
        params.setKind(kind);
        params.setCharCode(charCode);
        params.setTranSeq(tranSeq);
        mWealthManagementService.psnXpadReferProfitQuery(params)
                .compose(this.<PsnXpadReferProfitQueryResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnXpadReferProfitQueryResult>applyComputationSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnXpadReferProfitQueryResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mNetValueDetailView.obtainPsnXpadReferProfitQueryFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnXpadReferProfitQueryResult psnXpadReferProfitQueryResults) {
                        mNetValueDetailView.obtainPsnXpadReferProfitQuerySuccess(
                                mFinancialPositionCodeModeUtil.transverterPsnXpadReferProfitQueryResmodel(
                                        psnXpadReferProfitQueryResults));
                    }

                    @Override
                    public void commonHandleException(BiiResultErrorException biiResultErrorException) {
//                        super.commonHandleException(biiResultErrorException);
                    }
                });
    }

    /**
     * 4.14 014修改分红方式交易 PsnXpadSetBonusMode
     *
     * @param mConversationId
     * @param mCurrentBonusMode
     * @param banlanceDeta      36持仓列表接口
     */
    @Override
    public void getPsnXpadSetBonusMode(final String mConversationId, final String mCurrentBonusMode, final PsnXpadProductBalanceQueryResModel banlanceDeta) {
        PSNGetTokenIdParams psnGetTokenIdParams = new PSNGetTokenIdParams();
        psnGetTokenIdParams.setConversationId(mConversationId);
        // 查询TokenID
        globalService.psnGetTokenId(psnGetTokenIdParams)
                .compose(this.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnXpadSetBonusModeResult>>() {
                    @Override
                    public Observable<PsnXpadSetBonusModeResult> call(String tokenID) {
                        PsnXpadSetBonusModeParams mParams = new PsnXpadSetBonusModeParams();
                        mParams.setConversationId(mConversationId);
                        mParams.setToken(tokenID);
                        mParams.setAccountKey(banlanceDeta.getBancAccountKey());
                        mParams.setProdCode(banlanceDeta.getProdCode());
                        mParams.setProdName(banlanceDeta.getProdName());
                        mParams.setCurrencyCode(banlanceDeta.getCurCode());
                        mParams.setMode(mCurrentBonusMode);
                        mParams.setCashRemit(banlanceDeta.getCashRemit());
                        return mWealthManagementService.psnXpadSetBonusMode(mParams);
                    }
                }).compose(SchedulersCompat.<PsnXpadSetBonusModeResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnXpadSetBonusModeResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mNetValueDetailView.obtainPsnXpadSetBonusModeFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnXpadSetBonusModeResult doPaymentResult) {
                        mNetValueDetailView.obtainPsnXpadSetBonusModeSuccess(mFinancialPositionCodeModeUtil.transverterPsnXpadSetBonusMode(doPaymentResult));
                    }
                });
    }

}
