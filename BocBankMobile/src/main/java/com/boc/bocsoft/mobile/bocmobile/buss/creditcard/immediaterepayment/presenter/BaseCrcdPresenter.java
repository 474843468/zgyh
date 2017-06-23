package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.immediaterepayment.presenter;

import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetTokenId.PSNGetTokenIdParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import rx.Observable;
import rx.functions.Func1;

/**
 * 作者：xwg on 16/11/23 16:28
 * 信用卡公共部分
 */
public abstract class BaseCrcdPresenter extends RxPresenter{

    /**
     * 会话Id-一种交易获取token,预交易,交易使用一个conversationId
     */
    private String conversationId;
    /**
     * 全局Service-查询随机数,安全因子,会话,TOKEN等操作
     */
    private GlobalService globalService;

    public GlobalService getGlobalService() {
        if (globalService == null)
            globalService = new GlobalService();
        return globalService;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    /**
     * 清除会话
     */
    public void clearConversation() {
        conversationId = null;
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
        return getGlobalService().psnCreatConversation(psnCreatConversationParams);
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
                return getGlobalService().psnGetTokenId(params);
            }
        });
    }
}
