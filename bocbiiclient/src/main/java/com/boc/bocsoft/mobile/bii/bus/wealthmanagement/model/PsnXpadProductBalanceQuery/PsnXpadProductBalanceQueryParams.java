package com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProductBalanceQuery;

/**
 *   PsnXpadProductBalanceQuery  请求Model
 * Created by cff on 2016/9/7.
 */
public class PsnXpadProductBalanceQueryParams {

    private String accountKey;
    private String issueType;

    public String getAccountKey() {
        return accountKey;
    }

    public void setAccountKey(String accountKey) {
        this.accountKey = accountKey;
    }

    public String getIssueType() {
        return issueType;
    }

    public void setIssueType(String issueType) {
        this.issueType = issueType;
    }
}
