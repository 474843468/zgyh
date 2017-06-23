package com.boc.bocsoft.mobile.bii.bus.fund.model.PsnQueryTaAccountDetail;

/**
 *  I41 基金044接口 Ta账户信息查询
 * Created by lyf7084 on 2016/11/28.
 */
public class PsnQueryTaAccountDetailResultBean {
    /**
     * accountStatus : 00
     * fundAccount : null
     * taAccountNo : 622078115671
     * fundSeq : null
     * isPosition : N
     * fundRegCode : 11
     * isTrans : N
     * fundRegName : 注册登记机构名称
     */
    private String accountStatus;
    private String fundAccount;
    private String taAccountNo;
    private String fundSeq;
    private String isPosition;
    private String fundRegCode;
    private String isTrans;
    private String fundRegName;

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    public void setFundAccount(String fundAccount) {
        this.fundAccount = fundAccount;
    }

    public void setTaAccountNo(String taAccountNo) {
        this.taAccountNo = taAccountNo;
    }

    public void setFundSeq(String fundSeq) {
        this.fundSeq = fundSeq;
    }

    public void setIsPosition(String isPosition) {
        this.isPosition = isPosition;
    }

    public void setFundRegCode(String fundRegCode) {
        this.fundRegCode = fundRegCode;
    }

    public void setIsTrans(String isTrans) {
        this.isTrans = isTrans;
    }

    public void setFundRegName(String fundRegName) {
        this.fundRegName = fundRegName;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public String getFundAccount() {
        return fundAccount;
    }

    public String getTaAccountNo() {
        return taAccountNo;
    }

    public String getFundSeq() {
        return fundSeq;
    }

    public String getIsPosition() {
        return isPosition;
    }

    public String getFundRegCode() {
        return fundRegCode;
    }

    public String getIsTrans() {
        return isTrans;
    }

    public String getFundRegName() {
        return fundRegName;
    }
}