package com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGBailDetailQuery;

/**
 * Params：查询保证金账户详情
 * Created by zhx on 2016/11/21
 */
public class PsnVFGBailDetailQueryParams {

    /**
     * settleCurrency : 001
     * accountNumber : 4563510800034881051
     */
    // 借记卡卡号
    private String accountNumber;
    // 结算货币(014美元 001人民币 038欧元 013港币 027日元 029澳元)
    private String settleCurrency;

    public void setSettleCurrency(String settleCurrency) {
        this.settleCurrency = settleCurrency;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getSettleCurrency() {
        return settleCurrency;
    }

    public String getAccountNumber() {
        return accountNumber;
    }
}
