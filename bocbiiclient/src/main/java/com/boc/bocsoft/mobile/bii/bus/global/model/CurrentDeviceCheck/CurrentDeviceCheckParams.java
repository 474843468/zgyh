package com.boc.bocsoft.mobile.bii.bus.global.model.CurrentDeviceCheck;

/**
 * Created by feibin on 2016/6/7.
 *检查本机是否绑定
 */
public class CurrentDeviceCheckParams {
    private String conversationId;
    //使用硬件信息加密组件加密
    private String deviceInfo;

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    private String deviceInfo_RC;

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    private String activ;
    private String state;

    public String getDeviceInfo_RC() {
        return deviceInfo_RC;
    }

    public void setDeviceInfo_RC(String deviceInfo_RC) {
        this.deviceInfo_RC = deviceInfo_RC;
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

}
