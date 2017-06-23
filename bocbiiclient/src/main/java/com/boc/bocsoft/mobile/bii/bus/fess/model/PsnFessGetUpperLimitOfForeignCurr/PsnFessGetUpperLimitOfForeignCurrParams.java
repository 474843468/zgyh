package com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessGetUpperLimitOfForeignCurr;

/**
 * 4.15 015PsnFessGetUpperLimitOfForeignCurr可结售汇金额上限试算
 * Created by wzn7074 on 2016/11/16.
 * 需要与“PsnFessQueryAccount查询结售汇账户列表”接口使用同一conversation
 */
public class PsnFessGetUpperLimitOfForeignCurrParams {
    /**
     * annRmeAmtUSD : 50000.00
     * cashRemit : 02
     * availableBalanceRMB : 2000233.47
     * fessFlag : 11
     * currencyCode : 014
     */
    private String annRmeAmtUSD;//本年额度内剩余可/售结汇金额折美元
    private String cashRemit;//钞汇类型  01现钞 02现汇
    private String availableBalanceRMB;//人民币余额  业务类型为11-购汇时必输
    private String fessFlag;//结售汇业务类型 结汇：01 购汇：11
    private String currencyCode;//结购汇外币币种

    public void setAnnRmeAmtUSD(String annRmeAmtUSD) {
        this.annRmeAmtUSD = annRmeAmtUSD;
    }

    public void setCashRemit(String cashRemit) {
        this.cashRemit = cashRemit;
    }

    public void setAvailableBalanceRMB(String availableBalanceRMB) {
        this.availableBalanceRMB = availableBalanceRMB;
    }

    public void setFessFlag(String fessFlag) {
        this.fessFlag = fessFlag;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getAnnRmeAmtUSD() {
        return annRmeAmtUSD;
    }

    public String getCashRemit() {
        return cashRemit;
    }

    public String getAvailableBalanceRMB() {
        return availableBalanceRMB;
    }

    public String getFessFlag() {
        return fessFlag;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }
}
