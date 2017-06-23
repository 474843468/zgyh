package com.boc.bocsoft.mobile.bocmobile.buss.fund.fixedinvest.fixedpurchase.presenter;

import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundScheduledBuy.PsnFundScheduledBuyParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundScheduledBuy.PsnFundScheduledBuyResult;
import com.boc.bocsoft.mobile.bii.bus.fund.service.FundService;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetTokenId.PSNGetTokenIdParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.fixedinvest.fixedpurchase.model.PsnFundScheduleBuyModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.fixedinvest.fixedpurchase.ui.InvestConfirmContract;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by pactera on 2016/12/19.
 */

public class InvestConfirmPresenter extends RxPresenter implements InvestConfirmContract.Presenter{
    private InvestConfirmContract.InvestConfirmView investConfirmView;
    private FundService fundService;
    private String conversationId;
    private GlobalService globalService;

    public InvestConfirmPresenter(InvestConfirmContract.InvestConfirmView investConfirmView) {
        this.investConfirmView = investConfirmView;
        this.investConfirmView.setPresenter(this);
        fundService = new FundService();
        globalService = new GlobalService();
    }

    @Override
    public void psnFundScheduleBuy(PsnFundScheduleBuyModel model) {
        getToken().compose(this.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnFundScheduledBuyResult>>() {
                    @Override
                    public Observable<PsnFundScheduledBuyResult> call(String s) {
                        PsnFundScheduledBuyParams params = new PsnFundScheduledBuyParams();

                        return fundService.psnFundScheduledBuy(params);
                    }
                }).compose(SchedulersCompat.<PsnFundScheduledBuyResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnFundScheduledBuyResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        investConfirmView.psnFundScheduleBuyFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnFundScheduledBuyResult psnFundScheduledBuyResult) {
                        PsnFundScheduleBuyModel model1 = BeanConvertor.toBean(psnFundScheduledBuyResult,new PsnFundScheduleBuyModel());
                        investConfirmView.psnFundScheduleBuySuccess(model1);
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

}
