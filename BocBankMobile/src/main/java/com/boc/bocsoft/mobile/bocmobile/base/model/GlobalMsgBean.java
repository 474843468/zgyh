package com.boc.bocsoft.mobile.bocmobile.base.model;

/**
 * Created by feib on 16/7/22.
 */
public class GlobalMsgBean {
    private String content;
    private String subject;
    private String globalMsgId;
    private String popupFlag;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSubject() {
        return subject;
    }

    @Override
    public String toString() {
        return "GlobalMsgBean{" +
                "content='" + content + '\'' +
                ", subject='" + subject + '\'' +
                ", globalMsgId='" + globalMsgId + '\'' +
                ", popupFlag='" + popupFlag + '\'' +
                '}';
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getGlobalMsgId() {
        return globalMsgId;
    }

    public void setGlobalMsgId(String globalMsgId) {
        this.globalMsgId = globalMsgId;
    }

    public String getPopupFlag() {
        return popupFlag;
    }

    public void setPopupFlag(String popupFlag) {
        this.popupFlag = popupFlag;
    }
}
