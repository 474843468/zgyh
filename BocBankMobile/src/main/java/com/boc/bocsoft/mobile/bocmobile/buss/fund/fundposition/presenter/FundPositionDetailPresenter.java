package com.boc.bocsoft.mobile.bocmobile.buss.fund.fundposition.presenter;

import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundBonusResult.PsnFundBonusParmas;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundBonusResult.PsnFundBonusResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundNightBonusResult.PsnFundNightBonusParmas;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundNightBonusResult.PsnFundNightBonusResult;
import com.boc.bocsoft.mobile.bii.bus.fund.service.FundService;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetTokenId.PSNGetTokenIdParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.fundposition.model.FundPositionDetailModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.fundposition.ui.FundPositionDetailContract;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by taoyongzhen on 2016/12/8.
 */

public class FundPositionDetailPresenter extends RxPresenter implements FundPositionDetailContract.Presenter {

    private FundService fundService;
    private GlobalService globalService;
    private String conversationId;
    private FundPositionDetailContract.FundBonusDetailView fundBonusDetailView;

    public FundPositionDetailPresenter(FundPositionDetailContract.FundBonusDetailView fundBonusDetailView) {
        this.fundBonusDetailView = fundBonusDetailView;
        this.fundBonusDetailView.setPresenter(this);
        fundService = new FundService();
        globalService = new GlobalService();
    }

    @Override
    public void alterFundNightBonus(final FundPositionDetailModel params) {
        getToken().compose(this.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnFundNightBonusResult>>() {
                    @Override
                    public Observable<PsnFundNightBonusResult> call(String s) {
                        PsnFundNightBonusParmas psnFundBonusParmas = new PsnFundNightBonusParmas();
                        psnFundBonusParmas.setFundCode(params.getFundCode());
                        psnFundBonusParmas.setConversationId(conversationId);
                        psnFundBonusParmas.setToken(s);
                        psnFundBonusParmas.setFundBonusType(params.getFundBonusType());
                        psnFundBonusParmas.setAffirmFlag(params.getAffirmFlag());
                        return fundService.psnFundNightBonusResult(psnFundBonusParmas);
                    }
                })
                .compose(SchedulersCompat.<PsnFundNightBonusResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnFundNightBonusResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        fundBonusDetailView.alterFundNightBonusFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnFundNightBonusResult psnFundNightBonusResult) {
//                        FundPositionDetailModel model = new FundPositionDetailModel();
//                        model.setResult(psnFundNightBonusResult.getResult());
                        FundPositionDetailModel model = BeanConvertor.toBean(psnFundNightBonusResult,new FundPositionDetailModel());
                        fundBonusDetailView.alterFundBonusSuccess(model);
                    }
                });
    }

    @Override
    public void alterFundBonus(final FundPositionDetailModel params) {
        getToken().compose(this.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnFundBonusResult>>() {
                    @Override
                    public Observable<PsnFundBonusResult> call(String token) {
                        PsnFundBonusParmas psnFundBonusParmas = new PsnFundBonusParmas();
                        psnFundBonusParmas.setFundCode(params.getFundCode());
                        psnFundBonusParmas.setConversationId(conversationId);
                        psnFundBonusParmas.setToken(token);
                        psnFundBonusParmas.setFundBonusType(params.getFundBonusType());
                        psnFundBonusParmas.setAffirmFlag(params.getAffirmFlag());
                        return fundService.psnFundBonusResult(psnFundBonusParmas);
                    }
                })
                .compose(SchedulersCompat.<PsnFundBonusResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnFundBonusResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        fundBonusDetailView.alterFundBonusFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnFundBonusResult psnFundBonusResult) {
                        FundPositionDetailModel model = BeanConvertor.toBean(psnFundBonusResult,new FundPositionDetailModel());
                        fundBonusDetailView.alterFundBonusSuccess(model);

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
}
