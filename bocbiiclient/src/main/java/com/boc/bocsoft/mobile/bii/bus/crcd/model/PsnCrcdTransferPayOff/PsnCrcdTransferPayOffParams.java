package com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdTransferPayOff;

/**
 * 作者：xwg on 16/11/21 17:28
 * 关联信用卡还款确认
 */
public class PsnCrcdTransferPayOffParams {

    //转出账户标识
    private String fromAccountId;
    //转入账户标识
    private String toAccountId;
    //转账金额
    private String amount;
    //币种
    private String currency;
    /**
     * 钞汇标志	00：人民币、缺省值
     01：现钞
     02：现汇
     */
    private String cashRemit;
    //转入账户账号
    private String toAccount;
    //转入账户姓名
    private String toName;
    /**
     * 设备指纹	设备指纹字符串由客户浏览器运行的“rsa.js” Javascript脚本生成
     */
    private String devicePrint;
    private String token;
    private String conversationId;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCashRemit() {
        return cashRemit;
    }

    public void setCashRemit(String cashRemit) {
        this.cashRemit = cashRemit;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDevicePrint() {
        return devicePrint;
    }

    public void setDevicePrint(String devicePrint) {
        this.devicePrint = devicePrint;
    }

    public String getFromAccountId() {
        return fromAccountId;
    }

    public void setFromAccountId(String fromAccountId) {
        this.fromAccountId = fromAccountId;
    }

    public String getToAccount() {
        return toAccount;
    }

    public void setToAccount(String toAccount) {
        this.toAccount = toAccount;
    }

    public String getToAccountId() {
        return toAccountId;
    }

    public void setToAccountId(String toAccountId) {
        this.toAccountId = toAccountId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToName() {
        return toName;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }
}
