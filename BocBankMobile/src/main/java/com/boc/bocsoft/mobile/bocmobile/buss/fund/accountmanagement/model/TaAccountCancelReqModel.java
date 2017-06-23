package com.boc.bocsoft.mobile.bocmobile.buss.fund.accountmanagement.model;

import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnTaAccountCancel.PsnTaAccountCancelParams;

/**
 * 由045接口 PsnTaAccountCancel   Ta账户取消关联/销户
 * 上送参数：taAccountNo，transType，fundRegCode，token
 * 返回参数：fundSeq，taAccountNo，fundAccount，fundRegCode，FundRegName
 * Created by lyf7084 on 2016/12/13.
 */
public class TaAccountCancelReqModel {
    /**
     * TA账号
     */
    private String taAccountNo;

    /**
     * 交易类型
     */
    private String transType;

    /**
     * 注册基金公司代码
     */
    private String fundRegCode;

    /**
     * 防重机制token
     */
    private String token;

    /**
     * 会话ID
     */
    private String conversationId;

    public String getTaAccountNo() {
        return taAccountNo;
    }

    public void setTaAccountNo(String taAccountNo) {
        this.taAccountNo = taAccountNo;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public String getFundRegCode() {
        return fundRegCode;
    }

    public void setFundRegCode(String fundRegCode) {
        this.fundRegCode = fundRegCode;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

}
