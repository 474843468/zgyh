package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnQueryChinaBankAccount;

import java.util.List;

/**
 * Created by WYme on 2016/8/1.
 */
public class PsnQueryChinaBankAccountParams {

    /**
     * accountType : ["119","101","188","104","103","190","199","107"]
     * currentIndex : 0
     * pageSize : 10
     */

    private String currentIndex;
    private String pageSize;
    private List<String> accountType;

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

    public List<String> getAccountType() {
        return accountType;
    }

    public void setAccountType(List<String> accountType) {
        this.accountType = accountType;
    }
}
