package com.boc.bocsoft.mobile.bii.bus.account.model.PsnApplyTermDepositeResult;

/**
 * 账户结果参数返回
 * Created by Administrator on 2016/6/12.
 */
public class PsnApplyTermDepositeResult {

    /**
     * 申请账户状态 0 开户失败,1 开户成功
     */
    private String ApplyStatus;
    /**
     * 新开户的账号
     */
    private String AccountNum;
    /**
     * 关联网银状态 0 关联网银失败,1 关联网银成功
     */
    private String LinkStatus;

    public String getApplyStatus() {
        return ApplyStatus;
    }

    public void setApplyStatus(String ApplyStatus) {
        this.ApplyStatus = ApplyStatus;
    }

    public String getAccountNum() {
        return AccountNum;
    }

    public void setAccountNum(String AccountNum) {
        this.AccountNum = AccountNum;
    }

    public String getLinkStatus() {
        return LinkStatus;
    }

    public void setLinkStatus(String LinkStatus) {
        this.LinkStatus = LinkStatus;
    }
}

