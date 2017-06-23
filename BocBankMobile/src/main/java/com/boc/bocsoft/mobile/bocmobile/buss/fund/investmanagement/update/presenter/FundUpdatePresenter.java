package com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.update.presenter;

import com.boc.bocsoft.mobile.bii.bus.account.model.PsnQueryInvtBindingInfo.PsnQueryInvtBindingInfoParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnQueryInvtBindingInfo.PsnQueryInvtBindingInfoResult;
import com.boc.bocsoft.mobile.bii.bus.account.service.AccountService;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundCompanyInfoQuery.PsnFundCompanyInfoQueryParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundCompanyInfoQuery.PsnFundCompanyInfoQueryResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundQueryFundBalance.PsnFundQueryFundBalanceParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundQueryFundBalance.PsnFundQueryFundBalanceResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundScheduledBuyModify.PsnFundScheduledBuyModifyParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundScheduledBuyModify.PsnFundScheduledBuyModifyResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundScheduledSellModify.PsnFundScheduledSellModifyParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundScheduledSellModify.PsnFundScheduledSellModifyResullt;
import com.boc.bocsoft.mobile.bii.bus.fund.service.FundService;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetTokenId.PSNGetTokenIdParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.update.model.FundCompanyModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.update.model.FundScheduelBuyModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.update.model.FundScheduledBuyPms;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.update.model.FundScheduledSellModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.update.model.FundScheduledSellPms;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.update.ui.FundUpdateContract;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by huixiaobo on 2016/12/2.
 * 修改网络请求页面
 */
public class FundUpdatePresenter  extends RxPresenter implements FundUpdateContract.Presenter {

    /**公用上送Id*/
    private GlobalService mGlobalService;
    /**网络请求服务类*/
    private FundService mFundService;
    /***/
    private FundUpdateContract.InvestUpdateView mUpdateView;
    /***/
    private FundUpdateContract.UpdateInfoView mUpdateInfoView;
    /**定申修改会话Id*/
    private String mBconversationId;
    /**定赎修改会话Id*/
    private String mSconversationId;
    /**基金账号请求*/
    private AccountService accountService;

    /**修改首页请求*/
    public FundUpdatePresenter(FundUpdateContract.InvestUpdateView investUpdateView) {
        mUpdateView = investUpdateView;
        mUpdateView.setPresenter(this);
        mFundService = new FundService();
    }

    /**修改确认页面*/
    public FundUpdatePresenter(FundUpdateContract.UpdateInfoView updateInfoView) {
        mUpdateInfoView = updateInfoView;
        mUpdateInfoView.setPresenter(this);
        mGlobalService = new GlobalService();
        mFundService = new FundService();
        accountService = new AccountService();
    }

//    /**
//     * 015接口请求
//     */
//    @Override
//    public void qureyfundBalance(String fundcode) {
//        PsnFundQueryFundBalanceParams params = new PsnFundQueryFundBalanceParams();
//        params.setFundCode(fundcode);
//        mFundService.psnFundQueryFundBalance(params)
//                .compose(this.<PsnFundQueryFundBalanceResult>bindToLifecycle())
//                .compose(SchedulersCompat.<PsnFundQueryFundBalanceResult>applyIoSchedulers())
//                .subscribe(new BIIBaseSubscriber<PsnFundQueryFundBalanceResult>() {
//                    @Override
//                    public void handleException(BiiResultErrorException biiResultErrorException) {
//                        mUpdateView.fundBalanceFail(biiResultErrorException);
//                    }
//
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onNext(PsnFundQueryFundBalanceResult psnFundQueryFundBalanceResult) {
//                        mUpdateView.fundBalanceSuccess(null);
//                    }
//                });
//
//    }

//    /**061接口请求*/
//    @Override
//    public void queryFundCompany(String fundcode) {
//        PsnFundCompanyInfoQueryParams params = new PsnFundCompanyInfoQueryParams();
//        params.setFundCode(fundcode);
//        mFundService.psnFundCompanyInfoQuery(params)
//                .compose(this.<PsnFundCompanyInfoQueryResult>bindToLifecycle())
//                .compose(SchedulersCompat.<PsnFundCompanyInfoQueryResult>applyIoSchedulers())
//                .subscribe(new BIIBaseSubscriber<PsnFundCompanyInfoQueryResult>() {
//                    @Override
//                    public void handleException(BiiResultErrorException biiResultErrorException) {
//                        mUpdateInfoView.fundCompanyFail(biiResultErrorException);
//                    }
//
//                    @Override
//                    public void onCompleted() {
//                    }
//
//                    @Override
//                    public void onNext(PsnFundCompanyInfoQueryResult psnFundCompanyInfoQueryResult) {
//                        mUpdateInfoView.fundCompanySuccess(BeanConvertor.toBean(psnFundCompanyInfoQueryResult, new FundCompanyModel()));
//                    }
//                });
//
//    }

    @Override
    public void queryScheduledBuyModify(final FundScheduledBuyPms params) {
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
                .flatMap(new Func1<String, Observable<PsnFundScheduledBuyModifyResult>>() {
                    @Override
                    public Observable<PsnFundScheduledBuyModifyResult> call(String token) {
                        PsnFundScheduledBuyModifyParams bParams = BeanConvertor.toBean(params,new PsnFundScheduledBuyModifyParams() );
                        bParams.setConversationId(mBconversationId);
                        bParams.setToken(token);
                        return mFundService.psnFundScheduledBuyModify(bParams);
                    }
                })
                .compose(this.<PsnFundScheduledBuyModifyResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnFundScheduledBuyModifyResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnFundScheduledBuyModifyResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mUpdateInfoView.scheduledBuyModifyFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnFundScheduledBuyModifyResult psnFundScheduledBuyModifyResult) {
                        mUpdateInfoView.scheduledBuyModifySuccess(BeanConvertor.toBean(psnFundScheduledBuyModifyResult,
                                new FundScheduelBuyModel()));
                    }
                });

    }

    @Override
    public void queryScheduledSellModify(final FundScheduledSellPms params) {
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
                .flatMap(new Func1<String, Observable<PsnFundScheduledSellModifyResullt>>() {
                    @Override
                    public Observable<PsnFundScheduledSellModifyResullt> call(String token) {
                        PsnFundScheduledSellModifyParams sparams = BeanConvertor.toBean(params,new PsnFundScheduledSellModifyParams());
                        sparams.setConversationId(mSconversationId);
                        sparams.setToken(token);
                        return mFundService.psnFundScheduledSellModify(sparams);
                    }
                })
                .compose(this.<PsnFundScheduledSellModifyResullt>bindToLifecycle())
                .subscribe(new BIIBaseSubscriber<PsnFundScheduledSellModifyResullt>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mUpdateInfoView.scheduledSellModifyFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnFundScheduledSellModifyResullt psnFundScheduledSellModifyResullt) {
                        mUpdateInfoView.scheduledSellModifySuccess(BeanConvertor.toBean(psnFundScheduledSellModifyResullt,
                                new FundScheduledSellModel()));
                    }
                });
    }

    @Override
    public void queryAccount() {
        PsnQueryInvtBindingInfoParams params = new PsnQueryInvtBindingInfoParams();
        params.setInvtType("12");
        accountService.psnQueryInvtBindingInfo(params)
                .compose(this.<PsnQueryInvtBindingInfoResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnQueryInvtBindingInfoResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnQueryInvtBindingInfoResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mUpdateInfoView.fundAccountFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnQueryInvtBindingInfoResult result) {
                        mUpdateInfoView.fundAccountSuccess(result);
                    }
                });
    }

}
