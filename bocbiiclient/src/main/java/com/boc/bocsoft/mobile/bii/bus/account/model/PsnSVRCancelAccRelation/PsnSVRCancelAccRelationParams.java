package com.boc.bocsoft.mobile.bii.bus.account.model.PsnSVRCancelAccRelation;

import com.boc.bocsoft.mobile.bii.bus.account.model.PublicParams;

/**
 * @author wangyang
 *         16/7/7 10:31
 *         取消关联参数
 */
public class PsnSVRCancelAccRelationParams extends PublicParams {

    /** 账户Id */
    private String accountId;
    /** 账户 */
    private String accountNumber;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
}
