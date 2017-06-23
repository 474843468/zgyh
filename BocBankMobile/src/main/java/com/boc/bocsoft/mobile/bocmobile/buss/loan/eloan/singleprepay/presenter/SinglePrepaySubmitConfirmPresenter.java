package com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.singleprepay.presenter;

import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetTokenId.PSNGetTokenIdParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnELOANSingleRepaySubmit.PsnELOANSingleRepaySubmitParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnELOANSingleRepaySubmit.PsnELOANSingleRepaySubmitResponse;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnELOANSingleRepaySubmit.PsnELOANSingleRepaySubmitResult;
import com.boc.bocsoft.mobile.bii.bus.loan.service.PsnLoanService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.model.ErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.singleprepay.model.SinglePrepaySubmitReq;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.singleprepay.model.SinglePrepaySubmitRes;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.singleprepay.ui.SinglePrepaySubmitConfirmContract;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.singleprepay.ui.SinglePrepaySubmitContract;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import rx.Observable;
import rx.functions.Func1;

/**
 * 贷款管理-中银E贷-单笔交易提前还款确认
 * Created by liuzc on 2016/9/2.
 */
public class SinglePrepaySubmitConfirmPresenter extends RxPresenter implements SinglePrepaySubmitConfirmContract.Presenter{

    private SinglePrepaySubmitConfirmContract.View mContractView;
    private PsnLoanService mLoanService;
    private GlobalService globalService;

    /**
     * 会话Id-一种交易获取token,预交易,交易使用一个conversationId
     */
    private String conversationId;

    public SinglePrepaySubmitConfirmPresenter(SinglePrepaySubmitConfirmContract.View view){
        mContractView = view;
        mContractView.setPresenter(this);

        mLoanService = new PsnLoanService();
        globalService = new GlobalService();
    }

    public void setConversationId(String value){
        conversationId = value;
    }

    @Override
    public void prepaySubmit(final SinglePrepaySubmitReq params) {
//        SinglePrepaySubmitRes viewResult = new SinglePrepaySubmitRes();
//        viewResult.setLoanAccount("12323414124241");
//        viewResult.setTransactionId("233242345");
//        viewResult.setPayAccount("897932894");
//        viewResult.setAdvanceRepayCapital("30000");
//        viewResult.setAdvanceRepayInterest("22222");
//
//        mContractView.singlePrepaySubmitSuccess(viewResult);


         getToken().flatMap(new Func1<String, Observable<PsnELOANSingleRepaySubmitResult>>() {
                    @Override
                    public Observable<PsnELOANSingleRepaySubmitResult> call(String token) {

                        PsnELOANSingleRepaySubmitParams psnParams = buildSinglePrepaySubmitParams(params);
                        psnParams.setToken(token);
                        psnParams.setConversationId(conversationId);
                        return mLoanService.psnELOANSingleRepaySubmit(psnParams);
                    }
                })
                .compose(SchedulersCompat.<PsnELOANSingleRepaySubmitResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnELOANSingleRepaySubmitResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnELOANSingleRepaySubmitResult psnLOANRemainResult) {
                        SinglePrepaySubmitRes viewModel = getSinglePrepaySubmitResult(psnLOANRemainResult);
                        mContractView.singlePrepaySubmitSuccess(viewModel);
                    }

                    @Override
                    public void handleException(BiiResultErrorException e) {
                        ErrorException errorException = new ErrorException();
                        errorException.setErrorCode(e.getErrorCode());
                        errorException.setErrorMessage(e.getErrorMessage());
                        errorException.setErrorType(e.getErrorType());
                        mContractView.singlePrepaySubmitFail(errorException);
                    }
                });

    }

    /**
     * 获取会话,如果已经存在会话ID,则直接返回不用请求
     *
     * @return
     */
    public Observable<String> getConversation() {
        if (!StringUtils.isEmpty(conversationId))
            return Observable.just(conversationId);

        //生成ConversationId
        PSNCreatConversationParams psnCreatConversationParams = new PSNCreatConversationParams();
        return globalService.psnCreatConversation(psnCreatConversationParams);
    }

    /**
     * 获取Token
     *
     * @return
     */
    public Observable<String> getToken() {
        //根据ConversationId生成Token
        return getConversation().flatMap(new Func1<String, Observable<String>>() {
            @Override
            public Observable<String> call(String conversationResult) {
                conversationId = conversationResult;
                PSNGetTokenIdParams params = new PSNGetTokenIdParams();
                params.setConversationId(conversationResult);
                return globalService.psnGetTokenId(params);
            }
        });
    }

    /**
     * 构造请求参数
     * @param params
     * @return
     */
    private PsnELOANSingleRepaySubmitParams buildSinglePrepaySubmitParams(SinglePrepaySubmitReq params){
        PsnELOANSingleRepaySubmitParams result = new PsnELOANSingleRepaySubmitParams();

        result.setQuoteNo(params.getQuoteNo());
        result.setAccountId(params.getAccountId());
        result.setPayAccount(params.getPayAccount());
        result.setLoanType(params.getLoanType());
        result.setLoanAccount(params.getLoanAccount());
        result.setCurrency(params.getCurrency());
        result.setAdvanceRepayInterest(params.getAdvanceRepayInterest());
        result.setAdvanceRepayCapital(params.getAdvanceRepayCapital());
        result.setRepayAmount(params.getRepayAmount());

        return result;
    }

    /**
     * 根据网络请求结果构造页面展示信息
     * @param origResult
     * @return
     */
    private SinglePrepaySubmitRes getSinglePrepaySubmitResult(PsnELOANSingleRepaySubmitResult origResult){
        SinglePrepaySubmitRes viewResult = new SinglePrepaySubmitRes();

        viewResult.setLoanAccount(origResult.getLoanAccount());
        viewResult.setTransactionId(origResult.getTransactionId());
        viewResult.setPayAccount(origResult.getPayAccount());
        viewResult.setAdvanceRepayCapital(origResult.getAdvanceRepayCapital());
        viewResult.setAdvanceRepayInterest(origResult.getAdvanceRepayInterest());

        return viewResult;
    }
}
