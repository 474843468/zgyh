package com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdForeignPayOff;

/**
 * 作者：xwg on 16/11/21 18:52
 * 信用卡购汇还款提交
 */
public class PsnCrcdForeignPayOffParams {

    private String conversationId;


    /**
    *   转入账户标识		M
    */
    private String crcdId;
    /**
    *   信用卡持卡人姓名
    */
    private String crcdAcctName;
    /**
    *   信用卡卡号
    */
    private String crcdAcctNo;
    /**
    *   转出账户信息 M
    */
    private String rmbAccId;
    /**
    *   还款方式			“FULL”：全额还款;“MINP”：部分还款
     */
    private String crcdAutoRepayMode;
    private String token;
    /**
    *   还款金额（人民币元）	String		当“crcdAutoRepayMode”为“FULL”时可不输入，否则为必输
    */
    private String amount;
    /**
    *设备指纹
    */
    private String devicePrint;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getCrcdAcctName() {
        return crcdAcctName;
    }

    public void setCrcdAcctName(String crcdAcctName) {
        this.crcdAcctName = crcdAcctName;
    }

    public String getCrcdAcctNo() {
        return crcdAcctNo;
    }

    public void setCrcdAcctNo(String crcdAcctNo) {
        this.crcdAcctNo = crcdAcctNo;
    }

    public String getCrcdAutoRepayMode() {
        return crcdAutoRepayMode;
    }

    public void setCrcdAutoRepayMode(String crcdAutoRepayMode) {
        this.crcdAutoRepayMode = crcdAutoRepayMode;
    }

    public String getCrcdId() {
        return crcdId;
    }

    public void setCrcdId(String crcdId) {
        this.crcdId = crcdId;
    }

    public String getDevicePrint() {
        return devicePrint;
    }

    public void setDevicePrint(String devicePrint) {
        this.devicePrint = devicePrint;
    }

    public String getRmbAccId() {
        return rmbAccId;
    }

    public void setRmbAccId(String rmbAccId) {
        this.rmbAccId = rmbAccId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
