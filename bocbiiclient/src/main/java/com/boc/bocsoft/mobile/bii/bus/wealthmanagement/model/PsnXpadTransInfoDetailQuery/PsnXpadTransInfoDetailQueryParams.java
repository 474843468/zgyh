package com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadTransInfoDetailQuery;

/**
 * 交易状况详细信息查询
 * Created by zhx on 2016/9/5
 */
public class PsnXpadTransInfoDetailQueryParams {

    /**
     * accountKey : 97483dc7-885f-4f45-a2ad-e60f38e87573
     * tranSeq : 23423421
     */

    // 账号缓存标识（36位长度）
    private String accountKey;
    // 交易流水号（必输，由历史常规交易查询返回）
    private String tranSeq;

    public void setAccountKey(String accountKey) {
        this.accountKey = accountKey;
    }

    public void setTranSeq(String tranSeq) {
        this.tranSeq = tranSeq;
    }

    public String getAccountKey() {
        return accountKey;
    }

    public String getTranSeq() {
        return tranSeq;
    }
}
