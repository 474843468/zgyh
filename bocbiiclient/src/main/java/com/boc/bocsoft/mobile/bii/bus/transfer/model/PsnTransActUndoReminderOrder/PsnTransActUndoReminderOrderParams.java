package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActUndoReminderOrder;

/**
 * Params:撤消催款指令
 * Created by zhx on 2016/7/6
 */
public class PsnTransActUndoReminderOrderParams {

    /**
     * 指令序号
     */
    private String notifyId;
    /**
     * 防重机制，通过PSNGetTokenId接口获取
     */
    private String token;

    public String getNotifyId() {
        return notifyId;
    }

    public void setNotifyId(String notifyId) {
        this.notifyId = notifyId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
