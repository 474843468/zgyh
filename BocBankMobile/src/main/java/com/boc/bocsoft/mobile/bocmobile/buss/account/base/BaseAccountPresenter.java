package com.boc.bocsoft.mobile.bocmobile.buss.account.base;

import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetTokenId.PSNGetTokenIdParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnCommonQueryAllChinaBankAccount.PsnCommonQueryAllChinaBankAccountParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnCommonQueryAllChinaBankAccount.PsnCommonQueryAllChinaBankAccountResult;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.base.utils.AccountUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author wangyang
 *         16/6/28 14:23
 *         业务逻辑的公共部分
 */
public abstract class BaseAccountPresenter extends RxPresenter implements Presenter {

    /**
     * 会话Id-一种交易获取token,预交易,交易使用一个conversationId
     */
    private String conversationId;
    /**
     * 全局Service-查询随机数,安全因子,会话,TOKEN等操作
     */
    private GlobalService globalService;

    @Override
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
    @Override
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
    @Override
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

    /**
     * 刷新账户列表
     *
     * @param object
     */
    public void updateChinaBankAccountList(final Object object) {
        getGlobalService().psnCommonQueryAllChinaBankAccount(new PsnCommonQueryAllChinaBankAccountParams())
                .compose(this.<List<PsnCommonQueryAllChinaBankAccountResult>>bindToLifecycle())
                .compose(SchedulersCompat.<List<PsnCommonQueryAllChinaBankAccountResult>>applyIoSchedulers())
                .subscribe(new BaseAccountSubscriber<List<PsnCommonQueryAllChinaBankAccountResult>>() {
                    @Override
                    public void onNext(List<PsnCommonQueryAllChinaBankAccountResult> result) {
                        ApplicationContext.getInstance().setChinaBankAccountList(AccountUtils.convertBIIAccount2ViewModel(result));
                        afterUpdateChinaBankAccountList(object);
                    }
                });
    }

    /**
     * 刷新账户列表回调
     *
     * @param object
     */
    public void afterUpdateChinaBankAccountList(Object object) {

    }


}
