package com.boc.bocsoft.mobile.bocmobile.buss.fund.accountmanagement.model;

import com.boc.bocsoft.mobile.bocmobile.base.model.BaseFillInfoBean;

/**
 * 由010接口 PsnFundTAAccount 登记基金TA帐户
 * 上送参数：taAccount，token，regOrgCode
 * 返回参数：空
 * Created by lyf7084 on 2016/12/13.
 */
public class TaAccountRegisterModel{

    /**
     * taAccount : 123456789012
     * regOrgCode : 011
     * token : ccarucjw
     */
    private String taAccount;
    private String regOrgCode;
    private String token;
    private String conversationId; //会话ID

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public void setTaAccount(String taAccount) {
        this.taAccount = taAccount;
    }

    public void setRegOrgCode(String regOrgCode) {
        this.regOrgCode = regOrgCode;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTaAccount() {
        return taAccount;
    }

    public String getRegOrgCode() {
        return regOrgCode;
    }

    public String getToken() {
        return token;
    }
}