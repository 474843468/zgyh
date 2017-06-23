package com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadAutComTradStatus;

/**
 * 委托组合交易状况查询
 * Created by zhx on 2016/9/5
 */
public class PsnXpadAutComTradStatusParams {

    /**
     * startDate : 2019/01/26
     * accountKey : 97483dc7-885f-4f45-a2ad-e60f38e87573
     * endDate : 2019/02/01
     */

    // 账号缓存标识
    private String accountKey;
    // 起始日期（yyyy/mm/dd）
    private String startDate;
    // 结束日期（yyyy/mm/dd）
    private String endDate;

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setAccountKey(String accountKey) {
        this.accountKey = accountKey;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getAccountKey() {
        return accountKey;
    }

    public String getEndDate() {
        return endDate;
    }
}
