package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.billquery.unsettledbills.model;

/**
 * 作者：xwg on 16/12/23 15:44
 * 未出账单查询model
 */
public class CrcdBillQueryViewModel {

    private String accountId;
    private String pageSize;
    private String currentIndex;
    private String _refresh;

    public String get_refresh() {
        return _refresh;
    }

    public void set_refresh(String _refresh) {
        this._refresh = _refresh;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(String currentIndex) {
        this.currentIndex = currentIndex;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }
}
