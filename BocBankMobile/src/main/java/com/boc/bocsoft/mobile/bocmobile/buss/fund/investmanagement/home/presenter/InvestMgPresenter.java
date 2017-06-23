package com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.home.presenter;

import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundDdAbort.PsnFundDdAbortParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundDdAbort.PsnFundDdAbortResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundScheduledBuyDetailQuery.PsnFundScheduledBuyDetailQueryParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundScheduledBuyDetailQuery.PsnFundScheduledBuyDetailQueryResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundScheduledBuyPauseResume.PsnFundScheduledBuyPauseResumeParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundScheduledBuyPauseResume.PsnFundScheduledBuyPauseResumeResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundScheduledSellDetailQuery.PsnFundScheduledSellDetailQueryParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundScheduledSellDetailQuery.PsnFundScheduledSellDetailQueryResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundScheduledSellPauseResume.PsnFundScheduledSellPauseResumeParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundScheduledSellPauseResume.PsnFundScheduledSellPauseResumeResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundStatusDdApplyQuery.PsnFundStatusDdApplyQueryParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundStatusDdApplyQuery.PsnFundStatusDdApplyQueryResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnScheduledFundUnavailableQuery.PsnScheduledFundUnavailableQueryParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnScheduledFundUnavailableQuery.PsnScheduledFundUnavailableQueryResult;
import com.boc.bocsoft.mobile.bii.bus.fund.service.FundService;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetTokenId.PSNGetTokenIdParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.cancelorder.model.RedeemModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.home.model.InvalidinvestModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.home.model.InvestBuyDetailModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.home.model.InvestParams;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.home.model.InvestParamsModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.home.model.InvestSellDetailModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.home.model.InvestpsRsModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.home.model.RedeemParams;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.home.model.RedeempsRsModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.home.model.ValidinvestModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.home.ui.InvestMgContract;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by huixiaobo on 2016/11/24.
 * 定投管理网路请求管理类
 */
public class InvestMgPresenter extends RxPresenter implements InvestMgContract.Presenter {
    /**公用上送Id*/
    private GlobalService mGlobalService;
    /**有效定投请求*/
    private InvestMgContract.ValidinvestView mValidView;
    /**失效定投请求*/
    private InvestMgContract.InvestView mInvalidView;
    /**网络请求服务类*/
    private FundService mFundService;
    /**有效定投会话Id*/
    private String mInConversationId;
    /**失效定投会话Id*/
    private String mConversationId;
    /**详情view*/
    private InvestMgContract.InvestDetailView mDetailView;
    /**会话id*/
    private String mBconversationId;
    /**会话id*/
    private String mRconversationId;
    /**暂停恢复*/
    private  InvestMgContract.InvestPauseResumeView mPaReview;

    /**有效定投构造*/
    public InvestMgPresenter(InvestMgContract.ValidinvestView  validView) {
        mValidView = validView;
        mValidView.setPresenter(this);
        mGlobalService = new GlobalService();
        mFundService = new FundService();
    }

    /**失效定投构造*/
    public InvestMgPresenter(InvestMgContract.InvestView invalidView) {
        mInvalidView = invalidView;
        mInvalidView.setPresenter(this);
        mGlobalService = new GlobalService();
        mFundService = new FundService();
    }

    /**定投详情构造*/
    public InvestMgPresenter(InvestMgContract.InvestDetailView detailView, InvestMgContract.InvestPauseResumeView paReview) {
        mDetailView = detailView;
        mDetailView.setPresenter(this);
        mPaReview = paReview;
        mPaReview.setPresenter(this);
        mGlobalService = new GlobalService();
        mFundService = new FundService();
    }

    /**定投详情构造*/
    public InvestMgPresenter(InvestMgContract.InvestPauseResumeView paReview) {
        mPaReview = paReview;
        mPaReview.setPresenter(this);
        mGlobalService = new GlobalService();
        mFundService = new FundService();
    }



    /**011有效定投申请网络请求*/
    @Override
    public void queryValidinvest(final InvestParamsModel investParams) {
       if (mInConversationId != null) {
           queryWvalidinvest(investParams);
       } else {
           queryCvalidinvest(investParams);
       }
    }

    /**
     * 011接口请求会话Id
     * @param investParams
     */
    private void queryCvalidinvest(final InvestParamsModel investParams) {
        mGlobalService.psnCreatConversation(new PSNCreatConversationParams())
                .compose(this.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnFundStatusDdApplyQueryResult>>() {
                    @Override
                    public Observable<PsnFundStatusDdApplyQueryResult> call(String conversationId) {
                        mInConversationId = conversationId;
                        PsnFundStatusDdApplyQueryParams params = getValidinvestParams(investParams);
                        params.setConversationId(conversationId);
                        return mFundService.psnFundStatusDdApplyQuery(params);
                    }
                })
                .compose(this.<PsnFundStatusDdApplyQueryResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnFundStatusDdApplyQueryResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnFundStatusDdApplyQueryResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mValidView.fundValidinvestFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnFundStatusDdApplyQueryResult psnFundStatusDdApplyQueryResult) {
                        ValidinvestModel validinvestModel = builValidData(psnFundStatusDdApplyQueryResult);
                        mValidView.fundValidinvestSuccess(validinvestModel);
                    }
                });

    }

    /**
     * 011接口不请求会话Id接口
     * @param investParams
     */
    private void queryWvalidinvest(final InvestParamsModel investParams) {
        PsnFundStatusDdApplyQueryParams params = getValidinvestParams(investParams);
        params.setConversationId(mInConversationId);
        mFundService.psnFundStatusDdApplyQuery(params)
                .compose(this.<PsnFundStatusDdApplyQueryResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnFundStatusDdApplyQueryResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnFundStatusDdApplyQueryResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mValidView.fundValidinvestFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnFundStatusDdApplyQueryResult psnFundStatusDdApplyQueryResult) {
                        ValidinvestModel validinvestModel = builValidData(psnFundStatusDdApplyQueryResult);
                        mValidView.fundValidinvestSuccess(validinvestModel);
                    }
                });
    }



    /**058接口定投失效请求*/
    @Override
    public void queryInvalid(final InvestParamsModel investParams) {
        if (mConversationId != null) {
            queryWinvalid(investParams);
        } else {
            queryCinvalid(investParams);
        }
    }

    /**054接口详情网络请求*/
    @Override
    public void queryInvestBuyDetail(String date, String num) {
        PsnFundScheduledBuyDetailQueryParams params = new PsnFundScheduledBuyDetailQueryParams();
        params.setFundScheduleDate(date);
        params.setScheduleBuyNum(num);
        mFundService.psnFundScheduledBuyDetailQuery(params)
                .compose(this.<PsnFundScheduledBuyDetailQueryResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnFundScheduledBuyDetailQueryResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnFundScheduledBuyDetailQueryResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mDetailView.fundInvestBuyDetailFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnFundScheduledBuyDetailQueryResult psnFundScheduledBuyDetailQueryResult) {
                        InvestBuyDetailModel buyDetailModel = builBuyDetail(psnFundScheduledBuyDetailQueryResult);
                        mDetailView.fundInvestBuyDetailSuccess(buyDetailModel);
                    }
                });
    }

    /**055接口网络请求*/
    @Override
    public void queryInvestSellDetail(String date, String num) {
        PsnFundScheduledSellDetailQueryParams params = new PsnFundScheduledSellDetailQueryParams();
        params.setFundScheduleDate(date);
        params.setScheduleSellNum(num);
        mFundService.psnFundScheduledSellDetailQuery(params)
                .compose(this.<PsnFundScheduledSellDetailQueryResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnFundScheduledSellDetailQueryResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnFundScheduledSellDetailQueryResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mDetailView.fundInvestSellDetailFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnFundScheduledSellDetailQueryResult psnFundScheduledSellDetailQueryResult) {
                        InvestSellDetailModel sellDetailModel = builSellDetail(psnFundScheduledSellDetailQueryResult);
                        mDetailView.fundInvestSellDetailSuccess(sellDetailModel);
                    }
                });
    }

    /**
     * 056定投暂停恢复网络请求
     * @param params
     */
    @Override
    public void queryInvestPauseResume(final InvestParams params) {
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
                .flatMap(new Func1<String, Observable<PsnFundScheduledBuyPauseResumeResult>>() {
                    @Override
                    public Observable<PsnFundScheduledBuyPauseResumeResult> call(String token) {
                        PsnFundScheduledBuyPauseResumeParams inparams = setInvestReparams(params);
                        inparams.setToken(token);
                        inparams.setConversationId(mBconversationId);
                        return mFundService.psnFundScheduledBuyPauseResume(inparams);
                    }
                })
                .compose(SchedulersCompat.<PsnFundScheduledBuyPauseResumeResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnFundScheduledBuyPauseResumeResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mPaReview.fundInvestPauseResumeFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnFundScheduledBuyPauseResumeResult psnFundScheduledBuyPauseResumeResult) {
                        InvestpsRsModel investpsRsModel = builbuyPsRsData(psnFundScheduledBuyPauseResumeResult);
                        mPaReview.fundInvestPauseResumeSuccess(investpsRsModel);
                    }
                });
    }

    /**
     *057定赎暂停恢复网络请求
     * @param params
     */
    @Override
    public void queryRedeemPauseResume(final RedeemParams params) {
        mGlobalService.psnCreatConversation(new PSNCreatConversationParams())
                .compose(this.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String conversationId) {
                        mRconversationId = conversationId;
                        PSNGetTokenIdParams psnGetTokenIdParams = new PSNGetTokenIdParams();
                        psnGetTokenIdParams.setConversationId(conversationId);
                        return mGlobalService.psnGetTokenId(psnGetTokenIdParams);
                    }
                })
                .compose(this.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnFundScheduledSellPauseResumeResult>>() {
                    @Override
                    public Observable<PsnFundScheduledSellPauseResumeResult> call(String token) {
                        PsnFundScheduledSellPauseResumeParams reparams = setRedeemParams(params);
                        reparams.setToken(token);
                        reparams.setConversationId(mRconversationId);
                        return mFundService.psnFundScheduledSellPauseResume(reparams);
                    }
                })
                .compose(SchedulersCompat.<PsnFundScheduledSellPauseResumeResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnFundScheduledSellPauseResumeResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mPaReview.fundRedeemPauseResumeFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnFundScheduledSellPauseResumeResult psnFundScheduledSellPauseResumeResult) {
                        RedeempsRsModel rsModel = builSellPsRsData(psnFundScheduledSellPauseResumeResult);
                        mPaReview.fundRedeemPauseResumeSuccess(rsModel);
                    }
                });

    }

    /**
     * 058接口请求会话Id
     * @param investParams
     */
    private void queryCinvalid(final InvestParamsModel investParams) {
        mGlobalService.psnCreatConversation(new PSNCreatConversationParams())
                .compose(this.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnScheduledFundUnavailableQueryResult>>() {
                    @Override
                    public Observable<PsnScheduledFundUnavailableQueryResult> call(String conversationId) {
                        mConversationId = conversationId;
                        PsnScheduledFundUnavailableQueryParams params = getInvalidinvestParams(investParams);
                        params.setConversationId(conversationId);
                        return mFundService.psnScheduledFundUnavailableQuery(params);
                    }
                })
                .compose(this.<PsnScheduledFundUnavailableQueryResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnScheduledFundUnavailableQueryResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnScheduledFundUnavailableQueryResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mInvalidView.fundInvalidFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnScheduledFundUnavailableQueryResult psnScheduledFundUnavailableQueryResults) {
                        InvalidinvestModel invalidinvestModel = builtInvalid(psnScheduledFundUnavailableQueryResults);
                        mInvalidView.fundInvalidSuccess(invalidinvestModel);
                    }
                });
    }

    /**
     * 058接口没有请求会话Id
     * @param investParams
     */
    private void queryWinvalid(final InvestParamsModel investParams) {
        PsnScheduledFundUnavailableQueryParams params = getInvalidinvestParams(investParams);
        params.setConversationId(mConversationId);
        mFundService.psnScheduledFundUnavailableQuery(params)
                .compose(this.<PsnScheduledFundUnavailableQueryResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnScheduledFundUnavailableQueryResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnScheduledFundUnavailableQueryResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mInvalidView.fundInvalidFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnScheduledFundUnavailableQueryResult psnScheduledFundUnavailableQueryResult) {
                        InvalidinvestModel invalidinvestModel = builtInvalid(psnScheduledFundUnavailableQueryResult);
                        mInvalidView.fundInvalidSuccess(invalidinvestModel);
                    }
                });
    }

    /**
     * 011接口有效申请数据转换
     * @param result
     * @return
     */
    private ValidinvestModel builValidData(PsnFundStatusDdApplyQueryResult result) {
        ValidinvestModel validModel = null;
        if (result != null && result.getList() != null && result.getList().size() > 0) {
            validModel = new ValidinvestModel();
            validModel.setRecordNumber(result.getRecordNumber());
            List<ValidinvestModel.ListBean> fundBeanList = new ArrayList<ValidinvestModel.ListBean>();
            for (PsnFundStatusDdApplyQueryResult.ListBean funstatus : result.getList()) {
                ValidinvestModel.ListBean fundBean = BeanConvertor.toBean(funstatus, new ValidinvestModel.ListBean());
                ValidinvestModel.ListBean.FundInfoBean  fundInfoBean = BeanConvertor.toBean(funstatus.getFundInfo(),
                        new ValidinvestModel.ListBean.FundInfoBean());
                fundBean.setFundInfo(fundInfoBean);
                fundBeanList.add(fundBean);
            }
            validModel.setList(fundBeanList);
        }
        return validModel;
    }

    /**
     * 058接口失效申请数据转换
     * @param result
     * @return
     */
    private InvalidinvestModel builtInvalid(PsnScheduledFundUnavailableQueryResult result){
        InvalidinvestModel invalidModel = null;
        if (result != null && result.getList() != null && result.getList().size() > 0) {
            invalidModel = new InvalidinvestModel();
            invalidModel.setRecordNumber(result.getRecordNumber());
            List<InvalidinvestModel.ListBean> invalidlist = new ArrayList<InvalidinvestModel.ListBean>();
            for (PsnScheduledFundUnavailableQueryResult.ListBean invalidinvest : result.getList()) {
                InvalidinvestModel.ListBean invalid = BeanConvertor.toBean(invalidinvest,
                        new InvalidinvestModel.ListBean());
                InvalidinvestModel.ListBean.FundInfoBean fundInfoBean = BeanConvertor.toBean(invalidinvest.getFundInfo(),
                        new InvalidinvestModel.ListBean.FundInfoBean());
                invalid.setFundInfo(fundInfoBean);
                invalidlist.add(invalid);
            }
            invalidModel.setList(invalidlist);
        }
        return invalidModel;
    }

    /**
     * 有效定投申请上送参数
     * @param investParams
     * @return
     */
    private PsnFundStatusDdApplyQueryParams getValidinvestParams(InvestParamsModel investParams) {
        PsnFundStatusDdApplyQueryParams params = new PsnFundStatusDdApplyQueryParams();
        if (investParams != null) {
            params.setCurrentIndex(investParams.getCurrentIndex());
            params.setPageSize(investParams.getPageSize());
            params.setFundCode(investParams.getFundCode());
            params.setDtFlag(investParams.getDtFlag());
            params.set_refresh(investParams.is_refresh());
        }
        return params;
    }

    /**
     * 失效定投申请上送参数
     * @param investParams
     * @return
     */
    private PsnScheduledFundUnavailableQueryParams getInvalidinvestParams(InvestParamsModel investParams) {
        PsnScheduledFundUnavailableQueryParams params = new PsnScheduledFundUnavailableQueryParams();
        if (investParams != null) {
            params.setCurrentIndex(investParams.getCurrentIndex());
            params.setPageSize(investParams.getPageSize());
            params.set_refresh(investParams.is_refresh());

        }
        return params;
    }

    /**
     * 056接口上送参数
     *
     */
    private PsnFundScheduledBuyPauseResumeParams setInvestReparams(InvestParams inRsParams) {
        PsnFundScheduledBuyPauseResumeParams params = null;
        if (inRsParams != null) {
            params = new PsnFundScheduledBuyPauseResumeParams();
            params.setScheduleBuyNum(inRsParams.getScheduleBuyNum());
            params.setFundCode(inRsParams.getFundCode());
            params.setFundTransFlag(inRsParams.getFundTransFlag());
            params.setScheduleBuyDate(inRsParams.getScheduleBuyDate());
        }
        return params;
    }

    /**
     * 057接口上送参数
     */
    private PsnFundScheduledSellPauseResumeParams setRedeemParams(RedeemParams redeemParams) {
        PsnFundScheduledSellPauseResumeParams params = new PsnFundScheduledSellPauseResumeParams();
        if (redeemParams != null) {
            params.setFundTransFlag(redeemParams.getFundTransFlag());
            params.setFundCode(redeemParams.getFundCode());
            params.setScheduleSellDate(redeemParams.getScheduleSellDate());
            params.setScheduleSellNum(redeemParams.getScheduleSellNum());
        }
        return params;
    }


    /**
     * 054接口数据转换
     * @param result
     */
    private InvestBuyDetailModel builBuyDetail (PsnFundScheduledBuyDetailQueryResult result) {
        InvestBuyDetailModel buyDetailModel = null;
        if (result != null) {
            buyDetailModel = BeanConvertor.toBean(result, new InvestBuyDetailModel());
        }
        return buyDetailModel;
    }

    /**
     * 055接口数据转换
     * @param result
     */
    private InvestSellDetailModel builSellDetail(PsnFundScheduledSellDetailQueryResult result) {
        InvestSellDetailModel sellDetailModel = null;
        if (result != null) {
            sellDetailModel = BeanConvertor.toBean(result, new InvestSellDetailModel());
        }
        return sellDetailModel;
    }

    /**
     *056接口返回值转换
     *
     */
    private InvestpsRsModel builbuyPsRsData(PsnFundScheduledBuyPauseResumeResult result) {
        InvestpsRsModel inpsRsModel = null;
        if (result != null) {
            inpsRsModel = BeanConvertor.toBean(result, new InvestpsRsModel());
        }
        return inpsRsModel;
    }

    /**
     * 057接口返回值转换
     *
     */
    private RedeempsRsModel builSellPsRsData(PsnFundScheduledSellPauseResumeResult result) {
        RedeempsRsModel redeemModel = null;
        if (result != null) {
            redeemModel  = BeanConvertor.toBean(result, new RedeempsRsModel());
        }
        return redeemModel;
    }

}
