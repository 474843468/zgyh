package com.boc.bocsoft.mobile.bii.bus.setting.model.psnSvrGlobalMsgList;


/**
 * Created by feib on 16/7/6.
 */
public class PsnSvrGlobalMsgListResult {

    /**
     * content : 尊敬的客户，如果您收到任何可疑的电话或短信请千万不要相信
     * subject : 防诈骗安全提示
     * globalMsgId : 14
     * popupFlag : 1
     */

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
