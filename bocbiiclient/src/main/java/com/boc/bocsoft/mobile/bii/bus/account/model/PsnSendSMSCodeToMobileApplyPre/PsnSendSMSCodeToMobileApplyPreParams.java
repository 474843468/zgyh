package com.boc.bocsoft.mobile.bii.bus.account.model.PsnSendSMSCodeToMobileApplyPre;

/**
 *发送手机交易码请求
 * Created by liuyang on 2016/6/13.
 */
public class PsnSendSMSCodeToMobileApplyPreParams {


    /**
     * conversationId : b5864eb1-f26c-4339-a25b-9d68032ae118
     * id : 62
     * method : PsnSendSMSCodeToMobile
     * params : null
     */

    private String conversationId;
    private int id;
    private String method;
    private Object params;

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Object getParams() {
        return params;
    }

    public void setParams(Object params) {
        this.params = params;
    }
}
