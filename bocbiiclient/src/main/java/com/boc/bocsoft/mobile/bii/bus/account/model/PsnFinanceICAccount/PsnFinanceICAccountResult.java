package com.boc.bocsoft.mobile.bii.bus.account.model.PsnFinanceICAccount;

/**
 * @author wangyang
 *         16/6/17 09:25
 *         电子现金账户概览参数
 */
public class PsnFinanceICAccountResult {

    /** 是否申请Conversation */
    private boolean isConversation = false;
    /** 电子现金账户 */
    private String finanICNumber;
    /** 账户类型 */
    private String finanICType;
    /** 别名 */
    private String finanICName;
    /** 子账户类型 */
    private String finanSonType;
    /** 币种 */
    private String currencyCode;
    /** 账户Id */
    private String accountId;
    /** 账户别名 */
    private String nickName;

    public String getFinanICNumber() {
        return finanICNumber;
    }

    public void setFinanICNumber(String finanICNumber) {
        this.finanICNumber = finanICNumber;
    }

    public String getFinanICType() {
        return finanICType;
    }

    public void setFinanICType(String finanICType) {
        this.finanICType = finanICType;
    }

    public String getFinanICName() {
        return finanICName;
    }

    public void setFinanICName(String finanICName) {
        this.finanICName = finanICName;
    }

    public boolean isConversation() {
        return isConversation;
    }

    public String getFinanSonType() {
        return finanSonType;
    }

    public void setFinanSonType(String finanSonType) {
        this.finanSonType = finanSonType;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
