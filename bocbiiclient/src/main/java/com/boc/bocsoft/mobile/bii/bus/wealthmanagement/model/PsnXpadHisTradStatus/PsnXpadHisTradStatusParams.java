package com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadHisTradStatus;

/**
 * 历史常规交易状况查询
 * Created by zhx on 2016/9/5
 */
public class PsnXpadHisTradStatusParams {

    /**
     * currentIndex : 0
     * startDate : 2019/01/26
     * accountKey : 97483dc7-885f-4f45-a2ad-e60f38e87573
     * accountType : 119
     * pageSize : 15
     * xpadProductCurCode : 000
     * endDate : 2019/02/01
     * ibknum : 40740
     * _refresh : true
     */

    // 银行账号缓存标识（银行账号缓存标识）
    private String accountKey;
    // 000：全部、001：人民币元 014：美元 012：英镑 013：港币 028: 加拿大元 029：澳元 038：欧元 027：日元
    private String xpadProductCurCode;
    // 开始日期（yyyy/mm/dd）
    private String startDate;
    // 结束日期（yyyy/mm/dd）
    private String endDate;
    // 页面大小（数字必输）
    private String pageSize;
    // 当前页索引（数字必输）
    private String currentIndex;
    // true：重新查询结果(在交易改变查询条件时是需要重新查询的, currentIndex需上送0) false:不需要重新查询，使用缓存中的结果
    private String _refresh;
    private String accountType;
    private String ibknum;

    private String conversationId;

    public void setCurrentIndex(String currentIndex) {
        this.currentIndex = currentIndex;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setAccountKey(String accountKey) {
        this.accountKey = accountKey;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
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

    public String getAccountType() {
        return accountType;
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

    public String get_refresh() {
        return _refresh;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }
}
