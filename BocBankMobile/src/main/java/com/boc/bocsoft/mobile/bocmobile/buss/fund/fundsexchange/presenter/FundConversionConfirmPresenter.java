package com.boc.bocsoft.mobile.bocmobile.buss.fund.fundsexchange.presenter;

import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundCompanyInfoQuery.PsnFundCompanyInfoQueryParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundCompanyInfoQuery.PsnFundCompanyInfoQueryResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundConversionResult.PsnFundConversionParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundConversionResult.PsnFundConversionResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundNightConversionResult.PsnFundNightConversionParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundNightConversionResult.PsnFundNightConversionResult;
import com.boc.bocsoft.mobile.bii.bus.fund.service.FundService;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetTokenId.PSNGetTokenIdParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.fundsexchange.model.FundConversionConfirmModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.fundsexchange.ui.FundConversionConfirmContract;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by taoyongzhen on 2016/12/14.
 */

public class FundConversionConfirmPresenter extends RxPresenter implements FundConversionConfirmContract.Presenter {
    private FundService fundService;
    private GlobalService globalService;
    private String conversationId;
    private FundConversionConfirmContract.ConfirmConversionView confirmConversionView;

    public FundConversionConfirmPresenter(FundConversionConfirmContract.ConfirmConversionView confirmConversionView) {
        this.confirmConversionView = confirmConversionView;
        this.confirmConversionView.setPresenter(this);
        fundService = new FundService();
        globalService = new GlobalService();
    }

    @Override
    public void confirmConversion(final FundConversionConfirmModel params) {
        final PsnFundConversionParams psnFundConversionParams = new PsnFundConversionParams();
        getToken().compose(this.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnFundConversionResult>>() {
                    @Override
                    public Observable<PsnFundConversionResult> call(String token) {
                        psnFundConversionParams.setConversationId(conversationId);
                        psnFundConversionParams.setToken(token);
                        psnFundConversionParams.setFromFundCode(params.getFromFundCode());
                        psnFundConversionParams.setToFundCode(params.getToFundCode());
                        psnFundConversionParams.setSellFlag(params.getSellFlag());
                        psnFundConversionParams.setAmount(params.getAmount());
                        return fundService.psnFundConversionResult(psnFundConversionParams);
                    }
                })
                .compose(SchedulersCompat.<PsnFundConversionResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnFundConversionResult>() {
                     @Override
                     public void handleException(BiiResultErrorException biiResultErrorException) {
                          confirmConversionView.confirmConversionFail(biiResultErrorException);
                      }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnFundConversionResult psnFundConversionResult) {
                        FundConversionConfirmModel result = BeanConvertor.toBean(psnFundConversionResult, new FundConversionConfirmModel());
                        confirmConversionView.confirmConversionSuccess(result);
                    }
                 });
    }

    @Override
    public void confirmNightConversion(final FundConversionConfirmModel params) {
        final PsnFundNightConversionParams psnFundNightConversionParams = new PsnFundNightConversionParams();
        getToken().compose(this.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnFundNightConversionResult>>() {
                    @Override
                    public Observable<PsnFundNightConversionResult> call(String token) {
                        psnFundNightConversionParams.setConversationId(conversationId);
                        psnFundNightConversionParams.setToken(token);
                        psnFundNightConversionParams.setFromFundCode(params.getFromFundCode());
                        psnFundNightConversionParams.setToFundCode(params.getToFundCode());
                        psnFundNightConversionParams.setSellFlag(params.getSellFlag());
                        psnFundNightConversionParams.setAmount(params.getAmount());
                        return fundService.psnFundNightConversionResult(psnFundNightConversionParams);
                    }
                })
                .compose(SchedulersCompat.<PsnFundNightConversionResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnFundNightConversionResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        confirmConversionView.confirmNightConversionFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnFundNightConversionResult psnFundNightConversionResult) {
                        FundConversionConfirmModel model = BeanConvertor.toBean(psnFundNightConversionResult, new FundConversionConfirmModel());
                        confirmConversionView.confirmNightConversionSuccess(model);
                    }
                });

    }


    private Observable<String> getToken() {
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

    private Observable<String> getConversation() {
        if (!StringUtils.isEmpty(conversationId))
            return Observable.just(conversationId);

        //生成ConversationId
        PSNCreatConversationParams psnCreatConversationParams = new PSNCreatConversationParams();
        return globalService.psnCreatConversation(psnCreatConversationParams);
    }

    @Override
    public void queryFundCompanyInfo(String fundCode) {
        PsnFundCompanyInfoQueryParams params = new PsnFundCompanyInfoQueryParams();
        params.setFundCode(fundCode);
        fundService.psnFundCompanyInfoQuery(params)
                .compose(this.<PsnFundCompanyInfoQueryResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnFundCompanyInfoQueryResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnFundCompanyInfoQueryResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        confirmConversionView.queryFundCompanyInfoFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnFundCompanyInfoQueryResult result) {
                        confirmConversionView.queryFundCompanyInfoSuccess(result);
                    }
                });

    }

}
