package com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.cancelorder.presenter;

import com.boc.bocsoft.mobile.bii.bus.account.model.PsnQueryInvtBindingInfo.PsnQueryInvtBindingInfoParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnQueryInvtBindingInfo.PsnQueryInvtBindingInfoResult;
import com.boc.bocsoft.mobile.bii.bus.account.service.AccountService;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundDdAbort.PsnFundDdAbortParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundDdAbort.PsnFundDdAbortResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundScheduleSellCancel.PsnFundScheduleSellCancelParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundScheduleSellCancel.PsnFundScheduleSellCancelResult;
import com.boc.bocsoft.mobile.bii.bus.fund.service.FundService;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetTokenId.PSNGetTokenIdParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.fundproduct.home.model.InvstBindingInfoViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.cancelorder.model.InvestCldParamsModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.cancelorder.model.InvestModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.cancelorder.model.InvestRedeemParamsModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.cancelorder.model.RedeemModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.cancelorder.ui.InvestCldContract;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import java.math.BigDecimal;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by huixiaonbo on 2016/11/30.
 * 定投撤单网络请求
 */
public class InvestCldPresenter extends RxPresenter implements InvestCldContract.Presenter {
    /**公用上送Id*/
    private GlobalService mGlobalService;
    /**定投撤单veiw*/
    private InvestCldContract.CancelorderView mCancelorderView;
    /**定赎撤单view*/
    private InvestCldContract.RedeemView mRedeemCldView;
    /**网络请求服务类*/
    private FundService mFundService;
    /**定投会话Id*/
    private String mBconversationId;
    /**定赎会话Id*/
    private String mSconversationId;
    /**基金账号请求*/
    private AccountService accountService;

    /**定投撤单构造*/
    public InvestCldPresenter(InvestCldContract.CancelorderView cancelorderView) {
        mCancelorderView = cancelorderView;
        mCancelorderView.setPresenter(this);
        mGlobalService = new GlobalService();
        mFundService = new FundService();
        accountService = new AccountService();

    }

    /**定赎撤单构造*/
    public InvestCldPresenter(InvestCldContract.RedeemView redeemCldView) {
        mRedeemCldView = redeemCldView;
        mRedeemCldView.setPresenter(this);
        mGlobalService = new GlobalService();
        mFundService = new FundService();
        accountService = new AccountService();
    }

    /**
     * 014定投撤单网络请求
     */
    @Override
    public void queryFundDdAbort(final InvestCldParamsModel cldParamsModel) {
        mGlobalService.psnCreatConversation(new PSNCreatConversationParams())
                .compose(this.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String conversationId) {
                        mBconversationId = conversationId;
                        PSNGetTokenIdParams psnGetTokenIdParams = new PSNGetTokenIdParams();
                        psnGetTokenIdParams.setConversationId(conversationId);
                        return mGlobalService.psnGetTokenId(psnGetTokenIdParams);
                    }
                })
                .compose(this.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnFundDdAbortResult>>() {
                    @Override
                    public Observable<PsnFundDdAbortResult> call(String token) {
                        PsnFundDdAbortParams params = setBuyParams(cldParamsModel);
                        params.setToken(token);
                        params.setConversationId(mBconversationId);
                        return mFundService.psnFundDdAbort(params);
                    }
                })
                .compose(SchedulersCompat.<PsnFundDdAbortResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnFundDdAbortResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mCancelorderView.fundDdAbortFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnFundDdAbortResult psnFundDdAbortResult) {
                        InvestModel investModel = builInvestBuyData(psnFundDdAbortResult);
                        mCancelorderView.fundDdAbortSuccess(investModel);
                    }
                });
    }

    /**
     * 040定赎网络请求方法
     */
    @Override
    public void queryScheduleSellCancel(final InvestRedeemParamsModel paramsModel) {
        mGlobalService.psnCreatConversation(new PSNCreatConversationParams())
                .compose(this.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String conversationId) {
                        mSconversationId = conversationId;
                        PSNGetTokenIdParams psnGetTokenIdParams = new PSNGetTokenIdParams();
                        psnGetTokenIdParams.setConversationId(conversationId);
                        return mGlobalService.psnGetTokenId(psnGetTokenIdParams);
                    }
                })
                .compose(this.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnFundScheduleSellCancelResult>>() {
                    @Override
                    public Observable<PsnFundScheduleSellCancelResult> call(String token) {
                        PsnFundScheduleSellCancelParams params = setSellCancelParams(paramsModel);
                        params.setConversationId(mSconversationId);
                        params.setToken(token);
                        return mFundService.psnFundScheduleSellCancel(params);
                    }
                })
                .compose(SchedulersCompat.<PsnFundScheduleSellCancelResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnFundScheduleSellCancelResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mRedeemCldView.fundScheduleSellCancelFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnFundScheduleSellCancelResult psnFundScheduleSellCancelResult) {
                        RedeemModel redeemModel = builRedeemSellData(psnFundScheduleSellCancelResult);
                        mRedeemCldView.fundScheduleSellCancel(redeemModel);
                    }
                });

    }

    /**基金账号查询*/
    @Override
    public void queryAccount(final String invtType) {
        PsnQueryInvtBindingInfoParams params = new PsnQueryInvtBindingInfoParams();
        params.setInvtType("12");
        accountService.psnQueryInvtBindingInfo(params)
                .compose(this.<PsnQueryInvtBindingInfoResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnQueryInvtBindingInfoResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnQueryInvtBindingInfoResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        if ("1".equals(invtType)) {
                            mCancelorderView.fundAccountFail(biiResultErrorException);
                        } else if ("2".equals(invtType)){
                            mRedeemCldView.fundAccountFail(biiResultErrorException);
                        }
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnQueryInvtBindingInfoResult result) {
                        //InvstBindingInfoViewModel viewResult = BeanConvertor.toBean(result, new InvstBindingInfoViewModel());
                        if ("1".equals(invtType)) {
                            mCancelorderView.fundAccountSuccess(result);
                        } else if ("2".equals(invtType)) {
                            mRedeemCldView.fundAccountSuccess(result);
                        }
                    }
                });
    }

    /**
     * 014定投撤销上送参数
     * @param cldParams
     */
    private PsnFundDdAbortParams setBuyParams(InvestCldParamsModel cldParams) {
        PsnFundDdAbortParams params = null;
        if (cldParams != null) {
            params = new PsnFundDdAbortParams();
            params.setOldApplyDate(cldParams.getOldApplyDate());
            params.setFundCode(cldParams.getFundCode());
            params.setTransSeq(cldParams.getTransSeq());
            params.setEachAmount(cldParams.getEachAmount());
        }
       return params;
    }

    /**
     * 040接口上送参数
     * @param paramsModel
     */
    private PsnFundScheduleSellCancelParams setSellCancelParams(InvestRedeemParamsModel paramsModel) {
        PsnFundScheduleSellCancelParams params = null;
        if (paramsModel != null) {
            params = new PsnFundScheduleSellCancelParams();
            params.setEachAmount(paramsModel.getEachAmount());
            params.setOldApplyDate(paramsModel.getOldApplyDate());
            params.setFundCode(paramsModel.getFundCode());
            params.setTransSeq(paramsModel.getTransSeq());
        }
        return params;
    }

    /**
     * 014 接口返回参数转换
     */
    private InvestModel builInvestBuyData(PsnFundDdAbortResult result) {
        InvestModel investModel = null;
        if (result != null) {
           investModel = BeanConvertor.toBean(result, new InvestModel());
        }
        return investModel;
    }

    /**
     * 040接口返回参数转换
     * @param result
     */
    private RedeemModel builRedeemSellData(PsnFundScheduleSellCancelResult result) {
        RedeemModel redeemModel = null;
        if (result != null) {
            redeemModel = BeanConvertor.toBean(result, new RedeemModel());
        }
        return redeemModel;
    }

}
