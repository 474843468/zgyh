package com.boc.bocsoft.mobile.bocmobile.buss.fund.accountmanagement.presenter;

import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundChangeCard.PsnFundChangeCardResult;
import com.boc.bocsoft.mobile.bii.bus.fund.service.FundService;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetTokenId.PSNGetTokenIdParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.accountmanagement.model.ChangeCardReqModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.accountmanagement.ui.FundChangeCardContract;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import rx.Observable;
import rx.functions.Func1;


/**
 * 基金-账户管理-基金交易账户-变更资金帐户
 * 调用049接口 变更资金帐户
 * Created by lyf7084 on 2016/12/08.
 */

public class FundChangeCardPresenter extends RxPresenter
        implements FundChangeCardContract.Presenter {

    private FundChangeCardContract.View mContractView;
    private FundService mFundService;
    private GlobalService mGlobalService;
    //接口请求参数model
    private ChangeCardReqModel request;

    public FundChangeCardPresenter(FundChangeCardContract.View view) {

        mContractView = view;
        mFundService = new FundService();
        mGlobalService = new GlobalService();
    }


    /**
     * 创建会话
     */
    private String conversationId;
    private String tokenId;

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
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
                conversationId = conversationResult;
                PSNGetTokenIdParams params = new PSNGetTokenIdParams();
                params.setConversationId(conversationResult);
                return mGlobalService.psnGetTokenId(params);
            }
        });
    }

    @Override
    public void queryChangeCard(final ChangeCardReqModel request) {
        getToken().flatMap(new Func1<String, Observable<PsnFundChangeCardResult>>() {
            @Override
            public Observable<PsnFundChangeCardResult> call(String token) {
                request.setConversationId(conversationId);
                request.setToken(token);
                return mFundService.PsnFundChangeCard(request.getParams());
            }
             })
                .compose(this.<PsnFundChangeCardResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnFundChangeCardResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnFundChangeCardResult>() {

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnFundChangeCardResult result) {
                        //成功调用
                        mContractView.queryChangeCardSuccess();
                    }

                    @Override
                    public void handleException(BiiResultErrorException e) {
                        mContractView.queryChangeCardFail(e);
                    }
                });

    }
}