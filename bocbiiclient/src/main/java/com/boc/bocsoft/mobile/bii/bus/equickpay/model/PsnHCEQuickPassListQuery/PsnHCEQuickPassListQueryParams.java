package com.boc.bocsoft.mobile.bii.bus.equickpay.model.PsnHCEQuickPassListQuery;

/**
 * Created by gengjunying on 2016/12/6.
 * 获取hce卡列表信息 请求
 */
public class PsnHCEQuickPassListQueryParams {

    //设备imei
    private String deviceNo;
    //通道id
//    private String channelId;
    //会话id
    private String conversationId;

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getDeviceNo() {
        return deviceNo;
    }

    public void setDeviceNo(String deviceNo) {
        this.deviceNo = deviceNo;
    }

//    public String getChannelId() {
//        return channelId;
//    }

//    public void setChannelId(String channelId) {
//        this.channelId = channelId;
//    }

}
