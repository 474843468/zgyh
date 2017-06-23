package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model;

/**
 * ViewModel：双向宝-账户管理-保证金账户详情-双向宝解约
 * Created by zhx on 2016/11/23
 */
public class VFGCancelContractViewModel {
    //======================================//
    // 下面大致对应接口响应的字段
    //======================================//
    // 防重机制，通过PSNGetTokenId接口获取
    private String token;
    // 借记卡卡号
    private String accountNumber;
    // 保证金账号
    private String marginAccountNo;
    // 结算货币(014美元 001人民币 038欧元 013港币 027日元 029澳元)
    private String settleCurrency;

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getMarginAccountNo() {
        return marginAccountNo;
    }

    public void setMarginAccountNo(String marginAccountNo) {
        this.marginAccountNo = marginAccountNo;
    }

    public String getSettleCurrency() {
        return settleCurrency;
    }

    public void setSettleCurrency(String settleCurrency) {
        this.settleCurrency = settleCurrency;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
