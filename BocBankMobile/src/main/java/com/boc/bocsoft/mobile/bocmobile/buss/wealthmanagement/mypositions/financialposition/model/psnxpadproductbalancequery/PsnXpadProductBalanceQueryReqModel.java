package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.model.psnxpadproductbalancequery;

import java.io.Serializable;
/**
 * I42-4.36 036查询客户持仓信息 PsnXpadProductBalanceQuery
 *   PsnXpadProductBalanceQuery  请求Model
 * Created by cff on 2016/9/8.
 */
public class PsnXpadProductBalanceQueryReqModel implements Serializable{
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
