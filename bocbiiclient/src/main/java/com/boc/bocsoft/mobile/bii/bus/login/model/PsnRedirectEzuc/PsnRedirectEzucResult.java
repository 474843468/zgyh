package com.boc.bocsoft.mobile.bii.bus.login.model.PsnRedirectEzuc;

/**
 * 作者：XieDu
 * 创建时间：2016/10/20 20:20
 * 描述：互信跳转至易商
 */
public class PsnRedirectEzucResult {
    /**
     * custId
     */
    private String CID;
    /**
     * userId
     */
    private String UID;
    /**
     *
     */
    private String ticketInfo;

    public String getCID() {
        return CID;
    }

    public void setCID(String CID) {
        this.CID = CID;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getTicketInfo() {
        return ticketInfo;
    }

    public void setTicketInfo(String ticketInfo) {
        this.ticketInfo = ticketInfo;
    }
}
