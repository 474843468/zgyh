package com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdChargeOnRMBAccountQuery;

/**
 * 全球交易人民币记账功能查询
 * Created by wangf on 2016/11/22.
 */
public class PsnCrcdChargeOnRMBAccountQueryParams {

    //账户标识 - 只有双币信用卡才能调用此接口进行查询
    private String accountId;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
}
