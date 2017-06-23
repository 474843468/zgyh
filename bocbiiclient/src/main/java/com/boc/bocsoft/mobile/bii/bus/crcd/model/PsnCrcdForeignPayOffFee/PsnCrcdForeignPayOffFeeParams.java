package com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdForeignPayOffFee;

/**
 * 作者：xwg on 16/12/8 17:14
 *
 * 信用卡购汇还款手续费试算
 */
public class PsnCrcdForeignPayOffFeeParams {


    private String conversationId;
    private String crcdId;
    /**
    *  信用卡持卡人姓名
    */
    private String crcdAcctName;
    private String crcdAcctNo;

    /**
    *  转出账户信息
    */
    private String rmbAccId;

    /**
    *   还款方式  “MINP”--部分还款  "FULL"-- 全额还款
    */
    private String crcdAutoRepayMode;

    /**
    *   还款金额（人民币元）
     *   当“crcdAutoRepayMode”为“FULL”时可不输入，否则为必输
    */
    private String amount;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
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

    public String getRmbAccId() {
        return rmbAccId;
    }

    public void setRmbAccId(String rmbAccId) {
        this.rmbAccId = rmbAccId;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }
}
