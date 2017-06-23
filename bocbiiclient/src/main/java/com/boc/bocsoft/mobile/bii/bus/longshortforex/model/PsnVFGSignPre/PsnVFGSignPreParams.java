package com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGSignPre;

/**
 * Params：双向宝签约预交易
 * Created by zhx on 2016/11/21
 */
public class PsnVFGSignPreParams {

    /**
     * bailNo : 123
     * _combinId : 40
     * accountNumber : 4563510800034881051
     * accountId : 101020305
     * bailName : test
     */
    // 账户标识
    private String accountId;
    // 借记卡卡号
    private String accountNumber;
    // 保证金产品名称
    private String bailName;
    // 保证金产品序号
    private String bailNo;
    // 安全因子组合id(必输(服务码PB081))
    private String _combinId;
    private String conversationId;

    public void setBailNo(String bailNo) {
        this.bailNo = bailNo;
    }

    public void set_combinId(String _combinId) {
        this._combinId = _combinId;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public void setBailName(String bailName) {
        this.bailName = bailName;
    }

    public String getBailNo() {
        return bailNo;
    }

    public String get_combinId() {
        return _combinId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getBailName() {
        return bailName;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }
}
