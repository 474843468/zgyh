package com.boc.bocsoft.mobile.bii.bus.fund.model.PsnTaAccountCancel;

/**
 * Created by lyf7084 on 2016/11/24.
 */
public class PsnTaAccountCancelResult {

    /**
     * accountStatus : null
     * fundAccount : 123456789012345
     * taAccountNo : 123456789012
     * fundSeq : 123456789012
     * isPosition : null
     * fundRegCode : 11
     * isTrans : null
     * fundRegName : xxxxxxxxx
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

