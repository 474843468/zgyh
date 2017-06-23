package com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.model;

/**
 * 中行内汇款：新增收款人
 * Created by zhx on 2016/7/27
 */
public class PsnTransBocAddPayeeViewModel {
    /**
     * 转入账号
     */
    private String toAccountId;
    /**
     * 收款人姓名
     */
    private String payeeName;
    /**
     * 收款行行号
     */
    private String payeeBankNum;
    /**
     * 转入行类型
     */
    private String toAccountType;
    /**
     * 收款人手机
     */
    private String payeeMobile;

    public String getToAccountId() {
        return toAccountId;
    }

    public void setToAccountId(String toAccountId) {
        this.toAccountId = toAccountId;
    }

    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }

    public String getPayeeBankNum() {
        return payeeBankNum;
    }

    public void setPayeeBankNum(String payeeBankNum) {
        this.payeeBankNum = payeeBankNum;
    }

    public String getToAccountType() {
        return toAccountType;
    }

    public void setToAccountType(String toAccountType) {
        this.toAccountType = toAccountType;
    }

    public String getPayeeMobile() {
        return payeeMobile;
    }

    public void setPayeeMobile(String payeeMobile) {
        this.payeeMobile = payeeMobile;
    }
}
