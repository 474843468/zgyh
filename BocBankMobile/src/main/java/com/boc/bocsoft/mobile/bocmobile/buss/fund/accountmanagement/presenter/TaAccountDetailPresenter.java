package com.boc.bocsoft.mobile.bocmobile.buss.fund.accountmanagement.presenter;


import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnTaAccountCancel.PsnTaAccountCancelParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnTaAccountCancel.PsnTaAccountCancelResult;
import com.boc.bocsoft.mobile.bii.bus.fund.service.FundService;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetTokenId.PSNGetTokenIdParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.accountmanagement.model.TaAccountCancelReqModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.accountmanagement.model.TaAccountCancelResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.accountmanagement.ui.TaAccountDetailContract;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import rx.Observable;
import rx.functions.Func1;

public class TaAccountDetailPresenter extends RxPresenter implements TaAccountDetailContract.Presenter {

    private TaAccountDetailContract.View mTaAccountDetailView;
    private FundService mFundService;
    private GlobalService globalService;

    /**
     * 创建会话
     */
    private String conversationId;
    private String tokenId;

    public TaAccountDetailPresenter(TaAccountDetailContract.View mDetailView) {
        mTaAccountDetailView = mDetailView;
        mFundService = new FundService();
        globalService = new GlobalService();
        mTaAccountDetailView.setPresenter(this);
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    @Override
    public void taAccountCancel(TaAccountCancelReqModel params) {
        final PsnTaAccountCancelParams psnParams = new PsnTaAccountCancelParams();
        psnParams.setFundRegCode(params.getFundRegCode());
        psnParams.setTaAccountNo(params.getTaAccountNo());
        psnParams.setTransType(params.getTransType());

        getToken().flatMap(new Func1<String, Observable<PsnTaAccountCancelResult>>() {
            @Override
            public Observable<PsnTaAccountCancelResult> call(String token) {
                psnParams.setConversationId(conversationId);
                psnParams.setToken(token);
                return mFundService.PsnTaAccountCancel(psnParams);
            }
        })
                .compose(this.<PsnTaAccountCancelResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnTaAccountCancelResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnTaAccountCancelResult>() {

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnTaAccountCancelResult result) {
                        //成功调用
                        TaAccountCancelResModel modelRes = BeanConvertor.toBean(result, new TaAccountCancelResModel());
                        mTaAccountDetailView.onTaAccountCancelSuccess(modelRes);
                    }

                    @Override
                    public void handleException(BiiResultErrorException e) {
                        mTaAccountDetailView.onTaAccountCancelFail(e);
                    }
                });
    }

    public Observable<String> getConversation() {
//        if (!StringUtils.isEmpty(conversationId))
//            return Observable.just(conversationId);

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


}