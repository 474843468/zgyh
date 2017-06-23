package com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundSignElectronicContract;

/**
 * Created by zcy7065 on 2016/12/2.
 */
public class PsnFundSignElectronicContractParams {

    private String fincCode;//基金代码
    private String token;

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    private String conversationId;

    public void setFincCode(String fincCode) {
        this.fincCode = fincCode;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getFincCode() {
        return fincCode;
    }

    public String getToken() {
        return token;
    }
}
