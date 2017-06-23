package com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayGetPassFreeInfo;

/**
 * 查询小额免密信息
 * Created by wangf on 2016/8/30.
 */
public class QRPayGetPassFreeInfoResult {

    //信用卡免密开通标识
    private String creditCardFlag;
    //借记卡免密开通标识
    private String debitCardFlag;
    //信用卡免密金额
    private String creditCardPassFreeAmount;
    //借记卡免密金额
    private String debitCardPassFreeAmount;

    //606
    //免密开通标识
    private String passFreeFlag;
    //免密金额
    private String passFreeAmount;




    public String getCreditCardFlag() {
        return creditCardFlag;
    }

    public void setCreditCardFlag(String creditCardFlag) {
    	this.creditCardFlag = creditCardFlag;
    }

    public String getDebitCardFlag() {
        return debitCardFlag;
    }

    public void setDebitCardFlag(String debitCardFlag) {
    	this.debitCardFlag = debitCardFlag;
    }

    public String getCreditCardPassFreeAmount() {
        return creditCardPassFreeAmount;
    }

    public void setCreditCardPassFreeAmount(String creditCardPassFreeAmount) {
    	this.creditCardPassFreeAmount = creditCardPassFreeAmount;
    }

    public String getDebitCardPassFreeAmount() {
        return debitCardPassFreeAmount;
    }

    public void setDebitCardPassFreeAmount(String debitCardPassFreeAmount) {
    	this.debitCardPassFreeAmount = debitCardPassFreeAmount;
    }

    public String getPassFreeFlag() {
        return passFreeFlag;
    }

    public void setPassFreeFlag(String passFreeFlag) {
        this.passFreeFlag = passFreeFlag;
    }

    public String getPassFreeAmount() {
        return passFreeAmount;
    }

    public void setPassFreeAmount(String passFreeAmount) {
        this.passFreeAmount = passFreeAmount;
    }
}
