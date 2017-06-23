package com.boc.bocsoft.mobile.bocmobile.buss.account.base;

import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;

import rx.Observable;

/**
 * @author wangyang
 *         16/7/16 16:22
 *         账户模块逻辑公共部分接口
 */
public interface Presenter extends BasePresenter{

    /**
     * 获取公共GlobalService
     * @return
     */
    GlobalService getGlobalService();
    /**
     * 清除会话
     */
    void clearConversation();
    /**
     * 获取会话
     * @return
     */
    Observable<String> getConversation();
    /**
     * 获取Token
     * @return
     */
    Observable<String> getToken();
}
