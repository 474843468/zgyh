package com.boc.bocsoft.mobile.bii.bus.login.model.PsnSvrRegisterDevicePre;

/**
 * Created by feibin on 2016/6/20.
 * I20个人设定  4.75 075 PsnSvrRegisterDevicePre设备注册预交易
 * 请求参数model
 */
public class PsnSvrRegisterDevicePreParams {
    /**
     * _combinId : 32
     * conversationId : 86c22c10-bfca-4564-9729-945eea588c2e
     */

    /**
     * 安全因子组合id
     */
    private String _combinId;
    /**
     * 会话ID
     */
    private String conversationId;

    public String get_combinId() {
        return _combinId;
    }

    public void set_combinId(String _combinId) {
        this._combinId = _combinId;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }
}
