package com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayGetPayeeInfo;

/**
 * Created by fanbin on 16/10/8.
 */
public class QRPayGetPayeeInfoResult {
    //金额
    private String tranAmount;
    //收款人账号
    private String payeeActNum;
    //收款人姓名  带*号
    private String payeeActNam;
    //收款方附言  有则出现
    private String payeeComments;
    //收款行行号
    private String payeeIbkNum;
    //收款行名称
    private String payeeIbkNam;

    public String getTranAmount() {
        return tranAmount;
    }

    public void setTranAmount(String tranAmount) {
        this.tranAmount = tranAmount;
    }

    public String getPayeeActNum() {
        return payeeActNum;
    }

    public void setPayeeActNum(String payeeActNum) {
        this.payeeActNum = payeeActNum;
    }

    public String getPayeeActNam() {
        return payeeActNam;
    }

    public void setPayeeActNam(String payeeActNam) {
        this.payeeActNam = payeeActNam;
    }

    public String getPayeeComments() {
        return payeeComments;
    }

    public void setPayeeComments(String payeeComments) {
        this.payeeComments = payeeComments;
    }

    public String getPayeeIbkNum() {
        return payeeIbkNum;
    }

    public void setPayeeIbkNum(String payeeIbkNum) {
        this.payeeIbkNum = payeeIbkNum;
    }

    public String getPayeeIbkNam() {
        return payeeIbkNam;
    }

    public void setPayeeIbkNam(String payeeIbkNam) {
        this.payeeIbkNam = payeeIbkNam;
    }
}
