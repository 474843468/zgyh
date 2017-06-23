package com.boc.bocsoft.mobile.bii.bus.login.model.PsnSetMobileInfo;

/**
 * Created by feibin on 2016/10/24.
 */
public class PsnSetMobileInfoParams {
    private String conversationId;
    private String  token;
    private String  deviceInfo;
    private String devicestyle;
    private String  devicesubstyle;
    private String  pushAddress;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public String getDevicestyle() {
        return devicestyle;
    }

    public void setDevicestyle(String devicestyle) {
        this.devicestyle = devicestyle;
    }

    public String getDevicesubstyle() {
        return devicesubstyle;
    }

    public void setDevicesubstyle(String devicesubstyle) {
        this.devicesubstyle = devicesubstyle;
    }

    public String getPushAddress() {
        return pushAddress;
    }

    public void setPushAddress(String pushAddress) {
        this.pushAddress = pushAddress;
    }

    public String getBindFlag() {
        return bindFlag;
    }

    public void setBindFlag(String bindFlag) {
        this.bindFlag = bindFlag;
    }

    private String bindFlag;


    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }
}
