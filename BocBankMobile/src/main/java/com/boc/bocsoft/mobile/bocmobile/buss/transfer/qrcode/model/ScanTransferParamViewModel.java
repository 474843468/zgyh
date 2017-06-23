package com.boc.bocsoft.mobile.bocmobile.buss.transfer.qrcode.model;

import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.CombinListBean;

/**
 * Created by xdy4486 on 2016/6/28.
 */
public class ScanTransferParamViewModel {
    /**
     * 付款账户Id
     */
    private String fromAccountId;
    /**
     * 转账金额
     */
    private String amount;

    /**
     * 收款账号
     */
    private String payeeAccountNumber;
    /**
     * 收款人名
     */
    private String payeeName;

    /**
     * 收款人手机号
     */
    private String payeeMobile;

    /**
     * 附言
     */
    private String remark;

    /**
     * 安全因子
     */
    private CombinListBean combinListBean;

    public String getFromAccountId() {
        return fromAccountId;
    }

    public void setFromAccountId(String fromAccountId) {
        this.fromAccountId = fromAccountId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }

    public String getPayeeMobile() {
        return payeeMobile;
    }

    public void setPayeeMobile(String payeeMobile) {
        this.payeeMobile = payeeMobile;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public CombinListBean getCombinListBean() {
        return combinListBean;
    }

    public void setCombinListBean(CombinListBean combinListBean) {
        this.combinListBean = combinListBean;
    }

    public String getPayeeAccountNumber() {
        return payeeAccountNumber;
    }

    public void setPayeeAccountNumber(String payeeAccountNumber) {
        this.payeeAccountNumber = payeeAccountNumber;
    }
}
