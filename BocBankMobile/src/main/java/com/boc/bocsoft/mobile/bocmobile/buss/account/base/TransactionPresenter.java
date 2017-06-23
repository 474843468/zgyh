package com.boc.bocsoft.mobile.bocmobile.buss.account.base;

import com.boc.bocsoft.mobile.bocmobile.buss.account.SecurityModel;

/**
 * @author wangyang
 *         16/7/16 15:31
 *         账户模块业务逻辑公共部分接口
 */
public interface TransactionPresenter extends Presenter {

    /**
     * 获取安全因子
     * @param serviceId
     * @param view
     */
    void getSecurityCombin(final String serviceId, final BaseTransactionView view);
    /**
     * 获取随机数
     * @param view
     */
    void getRandom(final BaseTransactionView view);
    /**
     * 预交易回调
     * @param securityModel
     */
    void preTransactionSuccess(SecurityModel securityModel);
}
