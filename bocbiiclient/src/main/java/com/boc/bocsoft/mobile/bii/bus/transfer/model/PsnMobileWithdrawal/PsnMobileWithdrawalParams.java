package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnMobileWithdrawal;

/**
 * 汇款解付请求
 *
 * Created by liuweidong on 2016/7/12.
 */
public class PsnMobileWithdrawalParams {

    private String conversationId;
    private String activ;
    private String state;
    /**
     * 密码
     */
    private String withDrawPwd;
    /**
     * 加密后的密码
     */
    private String withDrawPwd_RC;
    /**
     * 汇款编号
     */
    private String remitNo;
    /**
     * 防重机制
     */
    private String token;
    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getActiv() {
        return activ;
    }

    public void setActiv(String activ) {
        this.activ = activ;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getWithDrawPwd() {
        return withDrawPwd;
    }

    public void setWithDrawPwd(String withDrawPwd) {
        this.withDrawPwd = withDrawPwd;
    }

    public String getWithDrawPwd_RC() {
        return withDrawPwd_RC;
    }

    public void setWithDrawPwd_RC(String withDrawPwd_RC) {
        this.withDrawPwd_RC = withDrawPwd_RC;
    }

    public String getRemitNo() {
        return remitNo;
    }

    public void setRemitNo(String remitNo) {
        this.remitNo = remitNo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
