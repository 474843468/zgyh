package com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANMultipleQuery;

import java.util.List;

/**
 * Created by XieDu on 2016/8/2.
 */
public class PsnLOANMultipleQueryParams {

    /**
     * 定一本账户Id
     */
    private String accountId;
    /**
     * 调用此接口上送的conversationId需与PsnLOANPledgeVerify、PsnLOANPledgeSubmit保持一致
     */
    private String conversationId;
    private List<CnyListEntity> cnyList;

    public String getAccountId() { return accountId;}

    public void setAccountId(String accountId) { this.accountId = accountId;}

    public String getConversationId() { return conversationId;}

    public void setConversationId(String conversationId) { this.conversationId = conversationId;}

    public List<CnyListEntity> getCnyList() { return cnyList;}

    public void setCnyList(List<CnyListEntity> cnyList) { this.cnyList = cnyList;}

    public static class CnyListEntity {
        private String currencyCode;

        public String getCurrencyCode() { return currencyCode;}

        public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode;}
    }
}
