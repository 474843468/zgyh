package com.boc.bocsoft.mobile.bii.bus.account.model.PsnAccountQryRecentTransDetail;

/**
 * @author wangyang
 *         16/8/16 18:29
 *         最近交易明细新接口
 */
public class PsnAccountQryRecentTransDetailParams {

    //private final String IS_MEDICAL_ACCOUNT = "1";

    /**
     * 账户Id
     */
    private String accountId;

    /**
     * 是否为医保账户 -- 若查询的是医保账户，则上送“1”，其他情况上送空
     */
    private String isMedicalAccount = "";

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getIsMedicalAccount() {
        return isMedicalAccount;
    }

    public void setIsMedicalAccount(String isMedicalAccount) {
        this.isMedicalAccount = isMedicalAccount;
    }

    public PsnAccountQryRecentTransDetailParams(String accountId, boolean isMedicalAccount) {
        this.accountId = accountId;
        if (isMedicalAccount)
            this.isMedicalAccount = "1";
    }

    public PsnAccountQryRecentTransDetailParams(String accountId) {
        this.accountId = accountId;
    }
}
