package com.boc.bocsoft.mobile.bocmobile.buss.fund.riskassessment.presenter;

import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundRiskEvaluationQuery.PsnFundRiskEvaluationQueryParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundRiskEvaluationQuery.PsnFundRiskEvaluationQueryResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundRiskEvaluationSubmit.PsnFundRiskEvaluationSubmitParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundRiskEvaluationSubmit.PsnFundRiskEvaluationSubmitResult;
import com.boc.bocsoft.mobile.bii.bus.fund.service.FundService;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetTokenId.PSNGetTokenIdParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.riskassessment.model.PsnFundRiskEvaluationQueryModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.riskassessment.model.PsnFundRiskEvaluationSubmitModel;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by taoyongzhen on 2016/11/19.
 */

public class FundRiskEvaluationPresenter extends RxPresenter implements FundRiskEvaluationContract.Presenter {


    FundRiskEvaluationContract.RiskEvaluationSubmitView riskEvaluationSubmitView;
    FundRiskEvaluationContract.RiskEvaluationResultView riskEvaluationResultView;

    FundService fundService;
    GlobalService globalService;
    String conversationId;

    public FundRiskEvaluationPresenter(FundRiskEvaluationContract.RiskEvaluationResultView riskEvaluationResultView) {
        this.riskEvaluationResultView = riskEvaluationResultView;
        globalService = new GlobalService();
        fundService = new FundService();
        riskEvaluationResultView.setPresenter(this);
    }

    public FundRiskEvaluationPresenter(FundRiskEvaluationContract.RiskEvaluationSubmitView riskEvaluationSubmitView) {
        this.riskEvaluationSubmitView = riskEvaluationSubmitView;
        globalService = new GlobalService();
        fundService = new FundService();
        riskEvaluationSubmitView.setPresenter(this);
    }


    @Override
    public void fundRiskEvaluationQuery() {
        PsnFundRiskEvaluationQueryParams  params = new PsnFundRiskEvaluationQueryParams();
        fundService.psnFundRiskEvaluationQuery(params)
                .compose(SchedulersCompat.<PsnFundRiskEvaluationQueryResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnFundRiskEvaluationQueryResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {

                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnFundRiskEvaluationQueryResult psnFundDetailQueryOutlayResult) {
                        PsnFundRiskEvaluationQueryModel model = new PsnFundRiskEvaluationQueryModel();
                        model.setIsEvaluated(psnFundDetailQueryOutlayResult.isEvaluated());
                        model.setRiskLevel(psnFundDetailQueryOutlayResult.getRiskLevel());
                        riskEvaluationResultView.fundRiskEvaluationInit(model);

                    }
                });
    }

    @Override
    public void fundRiskEvaluationSumbit() {
        getToken().compose(this.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnFundRiskEvaluationSubmitResult>>() {
                    @Override
                    public Observable<PsnFundRiskEvaluationSubmitResult> call(String token) {
                        PsnFundRiskEvaluationSubmitParams psnFundRiskEvaluationSubmitParams = new PsnFundRiskEvaluationSubmitParams();
                        psnFundRiskEvaluationSubmitParams.setToken(token);
                        psnFundRiskEvaluationSubmitParams.setRiskScore("0");
                        psnFundRiskEvaluationSubmitParams.setConversationId(conversationId);
                        return fundService.psnFundRiskEvaluationSubmit(psnFundRiskEvaluationSubmitParams);
                    }
                })
                .compose(SchedulersCompat.<PsnFundRiskEvaluationSubmitResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnFundRiskEvaluationSubmitResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {

                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnFundRiskEvaluationSubmitResult psnFundRiskEvaluationSubmitResult) {
                        PsnFundRiskEvaluationSubmitModel model = new PsnFundRiskEvaluationSubmitModel();
                        model.setEvaluationDate(psnFundRiskEvaluationSubmitResult.getEvaluationDate());
                        model.setRiskLevel(psnFundRiskEvaluationSubmitResult.getRiskLevel());
                        riskEvaluationResultView.fundRiskEvaluationResult(model);
//                        riskEvaluationSubmitView.fundRiskEvaluationSubmitResult();
                    }
                });

    }

    @Override
    public void fundRiskEvaluationSumbit(final PsnFundRiskEvaluationSubmitModel model) {
        getToken().compose(this.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnFundRiskEvaluationSubmitResult>>() {
                    @Override
                    public Observable<PsnFundRiskEvaluationSubmitResult> call(String token) {
                        PsnFundRiskEvaluationSubmitParams psnFundRiskEvaluationSubmitParams = new PsnFundRiskEvaluationSubmitParams();
                        psnFundRiskEvaluationSubmitParams.setToken(token);
                        psnFundRiskEvaluationSubmitParams.setConversationId(conversationId);
                        psnFundRiskEvaluationSubmitParams.setRiskScore(model.getRiskScore());
                        psnFundRiskEvaluationSubmitParams.setRiskAnswer(model.getRiskAnswer());
                        return fundService.psnFundRiskEvaluationSubmit(psnFundRiskEvaluationSubmitParams);
                    }
                })
                .compose(SchedulersCompat.<PsnFundRiskEvaluationSubmitResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnFundRiskEvaluationSubmitResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {

                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnFundRiskEvaluationSubmitResult psnFundRiskEvaluationSubmitResult) {
                        riskEvaluationSubmitView.fundRiskEvaluationSubmitResult();
                    }
                });

    }

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

    private Observable<String> getConversation(){
        if (!StringUtils.isEmpty(conversationId))
            return Observable.just(conversationId);

        //生成ConversationId
        PSNCreatConversationParams psnCreatConversationParams = new PSNCreatConversationParams();
        return globalService.psnCreatConversation(psnCreatConversationParams);
    }

}
