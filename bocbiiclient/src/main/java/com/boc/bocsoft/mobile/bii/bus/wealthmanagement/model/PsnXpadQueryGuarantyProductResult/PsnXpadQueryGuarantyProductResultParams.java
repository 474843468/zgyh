package com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadQueryGuarantyProductResult;

/**
 * 组合购买已押押品查询（详情）
 * Created by zhx on 2016/9/5
 */
public class PsnXpadQueryGuarantyProductResultParams {

    /**
     * accountKey : 97483dc7-885f-4f45-a2ad-e60f38e87573
     * ibknum : 40740
     * tranSeq : 4354354311115
     * typeOfAccount : SY
     */
    // 组合交易流水号
    private String tranSeq;
    // 账号缓存标识
    private String accountKey;
    // 省行联行号
    private String ibknum;
    // 账户类型（SY-活一本交易 CD-借记卡交易 MW-网上专属理财 GW-长城信用卡 必输）
    private String typeOfAccount;

    public void setAccountKey(String accountKey) {
        this.accountKey = accountKey;
    }

    public void setIbknum(String ibknum) {
        this.ibknum = ibknum;
    }

    public void setTranSeq(String tranSeq) {
        this.tranSeq = tranSeq;
    }

    public void setTypeOfAccount(String typeOfAccount) {
        this.typeOfAccount = typeOfAccount;
    }

    public String getAccountKey() {
        return accountKey;
    }

    public String getIbknum() {
        return ibknum;
    }

    public String getTranSeq() {
        return tranSeq;
    }

    public String getTypeOfAccount() {
        return typeOfAccount;
    }
}
