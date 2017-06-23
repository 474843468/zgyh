package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.repealorder.model;

/**
 * ViewModel：中银理财-常规委托交易撤单
 * Created by zhx on 2016/9/22
 */
public class XpadDelegateCancelViewModel {
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

    //======================================//
    // 下面大致对应接口响应的字段
    //======================================//

//    // 产品代码
//    private String prodCode;
    // 产品名称
//    private String prodName;
    // 交易类型（00：认购 01：申购 02：赎回 03：红利再投 04：红利发放 05：（经过）利息返还 06：本金返还 07：起息前赎回 08：利息折份额 09:赎回亏损 10:赎回盈利 11:产品转让）
    private String transType;
    // 交易币种（001：人民币元 014：美元 012：英镑 013：港币 028: 加拿大元 029：澳元 038：欧元 027：日元）
//    private String currencyCode;
    // 钞汇标识（01：钞 02：汇 00：人民币钞汇）
//    private String cashRemit;
    // 委托份额
    private String athNumber;
    // 委托金额
    private String athAmount;
    // 委托日期
    private String athDate;
    // 委托撤销日期
    private String cancelDate;
    // 交易流水号（网银）
    private String transactionId;

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public void setAthDate(String athDate) {
        this.athDate = athDate;
    }

    public void setCancelDate(String cancelDate) {
        this.cancelDate = cancelDate;
    }

    public void setAthAmount(String athAmount) {
        this.athAmount = athAmount;
    }

    public void setAthNumber(String athNumber) {
        this.athNumber = athNumber;
    }

    public String getTransType() {
        return transType;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getAthDate() {
        return athDate;
    }

    public String getCancelDate() {
        return cancelDate;
    }

    public String getAthAmount() {
        return athAmount;
    }

    public String getAthNumber() {
        return athNumber;
    }
}
