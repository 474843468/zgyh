package com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayOpenPassFreeServicePre;

/**
 * 开通小额免密服务预交易
 * Created by wangf on 2016/8/30.
 */
public class QRPayOpenPassFreeServicePreParams {

    //客户选择的安全验证工具id
    private String _combinId;
    //会话id
    private String conversationId;

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String get_combinId() {
        return _combinId;
    }

    public void set_combinId(String _combinId) {
        this._combinId = _combinId;
    }

}
