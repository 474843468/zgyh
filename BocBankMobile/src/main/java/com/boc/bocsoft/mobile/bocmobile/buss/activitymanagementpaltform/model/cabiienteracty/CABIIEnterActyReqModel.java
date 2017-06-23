package com.boc.bocsoft.mobile.bocmobile.buss.activitymanagementpaltform.model.cabiienteracty;

/**
 * 渠道前端给AMS传递票信息-首页活动管理平台-微信抽奖
 * 渠道前端使用post方式将上述字段传递给AMS
 * Created by yx on 2016/12/20.
 */

public class CABIIEnterActyReqModel {
    /**
     * 渠道  String {1：web，2：手机，4：homebank，5：微信，6：对接}
     */
    private String channel;
    /**
     * 票	String	票系统生成的票号
     */
    private String tokenCode;
    /**
     * 取票信息	String	customid|cif号|渠道|地区|
     */
    private String ticketMsg;

    public String getChannel() {
        return channel;
    }

    public String getTicketMsg() {
        return ticketMsg;
    }

    public String getTokenCode() {
        return tokenCode;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public void setTicketMsg(String ticketMsg) {
        this.ticketMsg = ticketMsg;
    }

    public void setTokenCode(String tokenCode) {
        this.tokenCode = tokenCode;
    }
}
