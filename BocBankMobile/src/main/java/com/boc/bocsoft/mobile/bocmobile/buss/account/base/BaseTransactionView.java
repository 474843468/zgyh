package com.boc.bocsoft.mobile.bocmobile.buss.account.base;

import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.DeviceInfoModel;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityFactorModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.SecurityModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

/**
 * @author wangyang
 *         16/8/19 18:42
 *         交易界面回调
 */
public interface BaseTransactionView extends BaseView<BasePresenter>{

    /**
     * 获取安全因子
     */
    void getSecurityCombin(String serviceId);

    /**
     * 安全因子回调
     *
     * @param securityModel
     */
    void psnCombinSuccess(SecurityFactorModel securityModel);

    /**
     * 获取随机数成功
     *
     * @param random
     * @param conversationId
     */
    void psnGetRandomSuccess(String random, String conversationId);

    /**
     * 预交易成功
     *
     * @param securityModel
     */
    void psnPreTransactionSuccess(SecurityModel securityModel);
    /**
     * 提交交易
     * @param deviceInfoModel
     * @param factorId
     * @param randomNums
     * @param encryptPasswords
     */
    void submitTransactionWithSecurity(DeviceInfoModel deviceInfoModel, final String factorId, final String[] randomNums, final String[] encryptPasswords);
}
