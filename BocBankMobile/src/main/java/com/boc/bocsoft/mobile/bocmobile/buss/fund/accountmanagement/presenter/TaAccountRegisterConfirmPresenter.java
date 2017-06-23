package com.boc.bocsoft.mobile.bocmobile.buss.fund.accountmanagement.presenter;

import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundTAAccount.PsnFundTAAccountParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundTAAccount.PsnFundTAAccountResult;
import com.boc.bocsoft.mobile.bii.bus.fund.service.FundService;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetTokenId.PSNGetTokenIdParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.accountmanagement.model.TaAccountRegisterModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.accountmanagement.ui.TaAccountRegisterConfirmContract;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import rx.Observable;
import rx.functions.Func1;

/**
 * 基金-账户管理-TA账户登记确认页Presenter
 * Created by lyf7084 on 2016/12/06
 */

public class TaAccountRegisterConfirmPresenter extends RxPresenter implements TaAccountRegisterConfirmContract.Presenter {

    private TaAccountRegisterConfirmContract.View mTaAccountRegisterConfirmView;
    private GlobalService mGlobalService;
    private FundService mFundService;    // 基金service
    private String mConversationId = null;  // Conversation ID

    public TaAccountRegisterConfirmPresenter(TaAccountRegisterConfirmContract.View view) {
        mTaAccountRegisterConfirmView = view;
        mTaAccountRegisterConfirmView.setPresenter(this);
        mGlobalService = new GlobalService();
        mFundService = new FundService();
    }

    public Observable<String> getConversation() {
        if (!StringUtils.isEmpty(mConversationId))
            return Observable.just(mConversationId);

        //生成ConversationId
        PSNCreatConversationParams psnCreatConversationParams = new PSNCreatConversationParams();
        return mGlobalService.psnCreatConversation(psnCreatConversationParams);
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
                mConversationId = conversationResult;
                PSNGetTokenIdParams params = new PSNGetTokenIdParams();
                params.setConversationId(conversationResult);
                return mGlobalService.psnGetTokenId(params);
            }
        });
    }

    @Override
    public void registerFundTaAccount(TaAccountRegisterModel model) {
        final PsnFundTAAccountParams params = new PsnFundTAAccountParams();
        params.setTaAccount(model.getTaAccount());
        params.setRegOrgCode(model.getRegOrgCode());

        getToken().flatMap(new Func1<String, Observable<PsnFundTAAccountResult>>() {
                    @Override
                    public Observable<PsnFundTAAccountResult> call(String token) {
                        params.setConversationId(mConversationId);
                        params.setToken(token);
                        return mFundService.PsnFundTAAccount(params);
                    }
                })
                .compose(this.<PsnFundTAAccountResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnFundTAAccountResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnFundTAAccountResult>() {

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnFundTAAccountResult result) {
                        //成功调用
                        mTaAccountRegisterConfirmView.registerFundTaAccountSuccess();
                    }

                    @Override
                    public void handleException(BiiResultErrorException e) {
                        mTaAccountRegisterConfirmView.registerFundTaAccountFail(e);
                    }
                });
    }
}
