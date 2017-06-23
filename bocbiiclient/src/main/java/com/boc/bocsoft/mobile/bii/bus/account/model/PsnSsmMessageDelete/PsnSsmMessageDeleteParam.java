package com.boc.bocsoft.mobile.bii.bus.account.model.PsnSsmMessageDelete;

/**
 * Created by pactera on 2016/7/12.
 */
public class PsnSsmMessageDeleteParam {

    private String conversationId;
    private String ssmserviceid;
    private String pushterm;

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }


    public String getSsmserviceid() {
        return ssmserviceid;
    }

    public void setSsmserviceid(String ssmserviceid) {
        this.ssmserviceid = ssmserviceid;
    }

    public String getPushterm() {
        return pushterm;
    }

    public void setPushterm(String pushterm) {
        this.pushterm = pushterm;
    }
}
