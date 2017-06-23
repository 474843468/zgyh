package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.presenter;

import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetTokenId.PSNGetTokenIdParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadReferProfitDetailQuery.PsnXpadReferProfitDetailQueryParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadReferProfitDetailQuery.PsnXpadReferProfitDetailQueryResult;
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
 * @author cff
 * @description 中银理财-持仓详情（收益累进）-Contract
 * @date 2016-9-22
 */
public class FinancialTypeEarBuildDetailPresenter extends RxPresenter implements FinancialPositionContract.FinancialTypeEarnBuildDetailPresenter {

    /**
     * 中银理财-service
     */
    private WealthManagementService mWealthManagementService;

    /**
     * 持仓列表-接口回调
     */
    private FinancialPositionContract.FinancialTypeEarnBuildDetailView mFinancialDetailView;
    /***
     * 持仓列表-model 转换工具类
     */
    private FinancialPositionCodeModeUtil mFinancialPositionCodeModeUtil;
    /**
     * 公共service
     */
    private GlobalService globalService;

    /**
     * 构造器，做初始化
     *
     * @param mFinancialDetailView
     */
    public FinancialTypeEarBuildDetailPresenter(FinancialPositionContract.FinancialTypeEarnBuildDetailView mFinancialDetailView) {
        this.mFinancialDetailView = mFinancialDetailView;
        mWealthManagementService = new WealthManagementService();
        globalService = new GlobalService();
    }

    /**
     * 4.59 059 参考收益详情查询 PsnXpadReferProfitDetailQuery
     *
     * @return
     * @author cff  2016-9-22
     */
    @Override
    public void getPsnXpadReferProfitDetailQuery(PsnXpadReferProfitDetailQueryParams params) {
        mWealthManagementService.psnXpadReferProfitDetailQuery(params)
                .compose(this.<PsnXpadReferProfitDetailQueryResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnXpadReferProfitDetailQueryResult>applyComputationSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnXpadReferProfitDetailQueryResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnXpadReferProfitDetailQueryResult psnXpadReferProfitDetailQueryResult) {
                        mFinancialDetailView.obtainPsnXpadReferProfitDetailQuerySuccess(mFinancialPositionCodeModeUtil.transverterPsnXpadReferProfitDetailQuery(psnXpadReferProfitDetailQueryResult));
                    }

                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mFinancialDetailView.obtainPsnXpadReferProfitDetailQueryFault();
                    }
                });
    }

    /**
     * 参考收益汇总查询
     *
     * @param accountKey  账号缓存标识(必选)
     * @param productCode 产品代码(必选)
     * @param kind        产品性质(必选)
     * @param charCode    钞汇标识(必输01：钞;02：汇;00：人民币)
     * @param tranSeq     份额流水号(业绩基准产品必输（PsnXpadProductBalanceQuery返回项standPro不为0的产品），取自PsnXpadQuantityDetail接口返回项tranSeq)
     */
    @Override
    public void getPsnXpadReferProfitQuery(String accountKey, String productCode, String kind, String charCode, String tranSeq) {
        PsnXpadReferProfitQueryParams params = new PsnXpadReferProfitQueryParams();
        if (null != accountKey) {
            params.setAccountKey(accountKey);
        }
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
                        mFinancialDetailView.obtainPsnXpadReferProfitQueryFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnXpadReferProfitQueryResult psnXpadReferProfitQueryResults) {
                        mFinancialDetailView.obtainPsnXpadReferProfitQuerySuccess(mFinancialPositionCodeModeUtil.transverterPsnXpadReferProfitQueryResmodel(psnXpadReferProfitQueryResults));
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
                        mFinancialDetailView.obtainPsnXpadSetBonusModeFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnXpadSetBonusModeResult doPaymentResult) {
                        mFinancialDetailView.obtainPsnXpadSetBonusModeSuccess(mFinancialPositionCodeModeUtil.transverterPsnXpadSetBonusMode(doPaymentResult));
                    }
                });
    }

}
