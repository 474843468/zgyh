package com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdReportLossResultReinforce;

/**
 * 信用卡挂失及补卡结果加强认证请求
 *
 * Created by liuweidong on 2016/6/13.
 */
public class PsnCrcdReportLossResultReinforceParams {

    /**
     * 会话ID
     */
    private String conversationId;
    /**
     * 设备指纹
     */
    private String devicePrint;
    private String activ;
    private String state;
    /**
     * 手机交易码
     */
    private String Smc;
    private String Smc_RC;
    /**
     * 防重机制token
     */
    private String token;

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getDevicePrint() {
        return devicePrint;
    }

    public void setDevicePrint(String devicePrint) {
        this.devicePrint = devicePrint;
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

    public String getSmc() {
        return Smc;
    }

    public void setSmc(String Smc) {
        this.Smc = Smc;
    }

    public String getSmc_RC() {
        return Smc_RC;
    }

    public void setSmc_RC(String Smc_RC) {
        this.Smc_RC = Smc_RC;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
