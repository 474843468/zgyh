package com.boc.bocsoft.mobile.bocmobile.buss.fund.fixedinvest.fixedredeem.presenter;

import com.boc.bocsoft.mobile.bii.bus.account.model.PsnQueryInvtBindingInfo.PsnQueryInvtBindingInfoParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnQueryInvtBindingInfo.PsnQueryInvtBindingInfoResult;
import com.boc.bocsoft.mobile.bii.bus.account.service.AccountService;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundCompanyInfoQuery.PsnFundCompanyInfoQueryParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundCompanyInfoQuery.PsnFundCompanyInfoQueryResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundScheduledBuy.PsnFundScheduledBuyParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundScheduledSell.PsnFundScheduledSellParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundScheduledSell.PsnFundScheduledSellResult;
import com.boc.bocsoft.mobile.bii.bus.fund.service.FundService;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetTokenId.PSNGetTokenIdParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.fixedinvest.fixedredeem.model.FundBalanceModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.fixedinvest.fixedredeem.model.InvtBindingInfoModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.fixedinvest.fixedredeem.model.PsnFundScheduledSellModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.fixedinvest.fixedredeem.ui.RedeemConfirmContract;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by pactera on 2016/12/19.
 */

public class RedeemConfirmPresenter extends RxPresenter implements RedeemConfirmContract.Presenter {

    private RedeemConfirmContract.RedeemConfirmView confirmView;
    private GlobalService globalService;
    private FundService fundService;
    private AccountService accountService;
    private String conversationId;

    public RedeemConfirmPresenter(RedeemConfirmContract.RedeemConfirmView confirmView) {
        this.confirmView = confirmView;
        confirmView.setPresenter(this);
        accountService = new AccountService();
        globalService = new GlobalService();
        fundService = new FundService();
    }


    @Override
    public void psnFundScheduledSell(final PsnFundScheduledSellModel model) {

        getToken().compose(this.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnFundScheduledSellResult>>() {
                    @Override
                    public Observable<PsnFundScheduledSellResult> call(String token) {
                        PsnFundScheduledSellParams params = new PsnFundScheduledSellParams();
                        params.setConversationId(conversationId);
                        params.setToken(token);
                        params.setFundCode(model.getFundCode());
                        params.setEachAmount(model.getEachAmount());
                        params.setDealCode(model.getDealCode());
                        params.setDtFlag(model.getDtFlag());
                        params.setEndSum(model.getEndSum());
                        return fundService.psnFundScheduledSell(params);
                    }
                }).compose(SchedulersCompat.<PsnFundScheduledSellResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnFundScheduledSellResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        confirmView.psnFundScheduledSellFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnFundScheduledSellResult psnFundScheduledSellResult) {
                        PsnFundScheduledSellModel model = BeanConvertor.toBean(psnFundScheduledSellResult,new PsnFundScheduledSellModel());
                        confirmView.psnFundScheduledSellSuccess(model);
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
