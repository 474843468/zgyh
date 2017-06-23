package com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadDelegateCancel;

/**
 * 常规委托交易撤单
 * Created by zhx on 2016/9/5
 */
public class PsnXpadDelegateCancelParams {

    /**
     * amount : 5000
     * cashRemit : 00
     * currencyCode : 001
     * prodName : RJYL-WY-he005认购
     * accountKey : 97483dc7-885f-4f45-a2ad-e60f38e87573
     * token : ccarucjw
     * prodCode : RJYL-WY-he005
     * conversationId : d22b34a3-58cd-427b-a02c-aa5892e0dbd3
     * transSeq : 22206201372472
     * entrustType : 1
     */
    // 账户缓存标识
    private String accountKey;
    // 交易流水号（后台）
    private String transSeq;
    // 委托业务类型（由委托常规交易状况查询接口返回 1：认购委托 2：挂单委托 3：预约额度委托 4：类基金申请委托 5：份额转换委托 6：指定日期赎回委托 7：申购申请委托 8：赎回申请委托 9：预约赎回委托 10提前赎回委托 11：申购委托 12：投资期数赎回委托 13：赎回委托）
    private String entrustType;
    // 防重标识
    private String token;
    // 产品代码（上送常规委托交易返回的对应字段）
    private String prodCode;
    // 产品名称（上送常规委托交易返回的对应字段）
    private String prodName;
    // 交易币种（上送常规委托交易返回的对应字段）
    private String currencyCode;
    // 钞汇标识（上送常规委托交易返回的对应字段）
    private String cashRemit;
    // 交易金额（上送常规委托交易返回的对应字段）
    private String amount;
    private String conversationId;

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setCashRemit(String cashRemit) {
        this.cashRemit = cashRemit;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public void setAccountKey(String accountKey) {
        this.accountKey = accountKey;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setProdCode(String prodCode) {
        this.prodCode = prodCode;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public void setTransSeq(String transSeq) {
        this.transSeq = transSeq;
    }

    public void setEntrustType(String entrustType) {
        this.entrustType = entrustType;
    }

    public String getAmount() {
        return amount;
    }

    public String getCashRemit() {
        return cashRemit;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public String getProdName() {
        return prodName;
    }

    public String getAccountKey() {
        return accountKey;
    }

    public String getToken() {
        return token;
    }

    public String getProdCode() {
        return prodCode;
    }

    public String getConversationId() {
        return conversationId;
    }

    public String getTransSeq() {
        return transSeq;
    }

    public String getEntrustType() {
        return entrustType;
    }
}
