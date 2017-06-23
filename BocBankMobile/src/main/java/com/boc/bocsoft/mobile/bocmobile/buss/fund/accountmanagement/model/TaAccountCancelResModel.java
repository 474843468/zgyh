package com.boc.bocsoft.mobile.bocmobile.buss.fund.accountmanagement.model;

/**
 * 由045接口 PsnTaAccountCancel   Ta账户取消关联/销户
 * 上送参数：taAccountNo，transType，fundRegCode，token
 * 返回参数：fundSeq，taAccountNo，fundAccount，fundRegCode，FundRegName
 * Created by lyf7084 on 2016/12/13.
 */
public class TaAccountCancelResModel {

    /**
     * 账户状态
     */
    private String accountStatus;

    /**
     * 基金交易账号
     */
    private String fundAccount;

    /**
     * TA账号
     */
    private String taAccountNo;

    /**
     * 基金交易流水号
     */
    private String fundSeq;

    /**
     * 是否在途，持仓
     */
    private String isPosition;
    private String isTrans;

    /**
     * 注册基金公司代码
     */
    private String fundRegCode;

    /**
     * 注册基金公司名称
     */
    private String fundRegName;

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    public String getFundAccount() {
        return fundAccount;
    }

    public void setFundAccount(String fundAccount) {
        this.fundAccount = fundAccount;
    }

    public String getTaAccountNo() {
        return taAccountNo;
    }

    public void setTaAccountNo(String taAccountNo) {
        this.taAccountNo = taAccountNo;
    }

    public String getFundSeq() {
        return fundSeq;
    }

    public void setFundSeq(String fundSeq) {
        this.fundSeq = fundSeq;
    }

    public String getIsPosition() {
        return isPosition;
    }

    public void setIsPosition(String isPosition) {
        this.isPosition = isPosition;
    }

    public String getFundRegCode() {
        return fundRegCode;
    }

    public void setFundRegCode(String fundRegCode) {
        this.fundRegCode = fundRegCode;
    }

    public String getIsTrans() {
        return isTrans;
    }

    public void setIsTrans(String isTrans) {
        this.isTrans = isTrans;
    }

    public String getFundRegName() {
        return fundRegName;
    }

    public void setFundRegName(String fundRegName) {
        this.fundRegName = fundRegName;
    }

}
