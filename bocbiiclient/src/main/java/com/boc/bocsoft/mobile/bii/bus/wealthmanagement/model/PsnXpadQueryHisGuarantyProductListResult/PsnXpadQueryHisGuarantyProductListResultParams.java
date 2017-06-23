package com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadQueryHisGuarantyProductListResult;

/**
 * 组合购买历史交易状况查询
 * Created by zhx on 2016/9/5
 */
public class PsnXpadQueryHisGuarantyProductListResultParams {

    /**
     * currentIndex : 0
     * startDate : 2019/01/26
     * accountKey : 97483dc7-885f-4f45-a2ad-e60f38e87573
     * pageSize : 15
     * xpadProductCurCode : 000
     * endDate : 2019/02/01
     * ibknum : 40740
     * brandId : 1
     * typeOfAccount : 119
     * _refresh : true
     */
    private String conversationId;

    // 币种（（默认为000） 000：全部、001：人民币元 014：美元 012：英镑 013：港币 028: 加拿大元 029：澳元 038：欧元 027：日元）
    private String xpadProductCurCode;
    // 起始日期
    private String startDate;
    // 结束日期
    private String endDate;
    // 账号缓存标识（36位长度）
    private String accountKey;
    // pageSize
    private String pageSize;
    // 当前页索引
    private String currentIndex;
    // true：重新查询结果(在交易改变查询条件时是需要重新查询的,（同其他刷新标识上送规则）
    private String _refresh;

    private String brandId;

    private String ibknum;

    private String typeOfAccount;

    public void setCurrentIndex(String currentIndex) {
        this.currentIndex = currentIndex;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setAccountKey(String accountKey) {
        this.accountKey = accountKey;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public void setXpadProductCurCode(String xpadProductCurCode) {
        this.xpadProductCurCode = xpadProductCurCode;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setIbknum(String ibknum) {
        this.ibknum = ibknum;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public void setTypeOfAccount(String typeOfAccount) {
        this.typeOfAccount = typeOfAccount;
    }

    public void set_refresh(String _refresh) {
        this._refresh = _refresh;
    }

    public String getCurrentIndex() {
        return currentIndex;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getAccountKey() {
        return accountKey;
    }

    public String getPageSize() {
        return pageSize;
    }

    public String getXpadProductCurCode() {
        return xpadProductCurCode;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getIbknum() {
        return ibknum;
    }

    public String getBrandId() {
        return brandId;
    }

    public String getTypeOfAccount() {
        return typeOfAccount;
    }

    public String get_refresh() {
        return _refresh;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }
}
