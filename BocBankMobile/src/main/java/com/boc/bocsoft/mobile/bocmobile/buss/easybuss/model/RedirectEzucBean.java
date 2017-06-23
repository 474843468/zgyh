package com.boc.bocsoft.mobile.bocmobile.buss.easybuss.model;

/**
 * 作者：XieDu
 * 创建时间：2016/10/17 10:39
 * 描述：
 */
public class RedirectEzucBean {

    /**
     * custId
     */
    private String custId;
    /**
     * userId
     */
    private String bocnetUserId;
    /**
     *
     */
    private String accessChannel;

    private String headerFlag;

    private String ticketValue;

    private String bocnetLoginName;

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getBocnetUserId() {
        return bocnetUserId;
    }

    public void setBocnetUserId(String bocnetUserId) {
        this.bocnetUserId = bocnetUserId;
    }

    public String getAccessChannel() {
        return accessChannel;
    }

    public void setAccessChannel(String accessChannel) {
        this.accessChannel = accessChannel;
    }

    public String getHeaderFlag() {
        return headerFlag;
    }

    public void setHeaderFlag(String headerFlag) {
        this.headerFlag = headerFlag;
    }

    public String getTicketValue() {
        return ticketValue;
    }

    public void setTicketValue(String ticketValue) {
        this.ticketValue = ticketValue;
    }

    public String getBocnetLoginName() {
        return bocnetLoginName;
    }

    public void setBocnetLoginName(String bocnetLoginName) {
        this.bocnetLoginName = bocnetLoginName;
    }
}
