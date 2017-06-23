package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransManagePayeeModifyMobile;

/**
 * 修改收款人手机号
 * Created by zhx on 2016/7/26
 */
public class PsnTransManagePayeeModifyMobileParams {
    /**
     * 收款人ID(这里是String，如果是Integer会报一大堆的错，错错错！)
     */
    private String payeeId;
    /**
     * 旧手机号
     */
    private String oldMobile;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 防重机制，通过PSNGetTokenId接口获取
     */
    private String token;
    /**
     * 设备指纹
     */
    private String devicePrint;
    /**
     * 收款人姓名
     */
    private String ToUserName;
    /**
     * 收款人账号
     */
    private String ToAccountNo;
    private String conversationId;

    public String getPayeeId() {
        return payeeId;
    }

    public void setPayeeId(String payeeId) {
        this.payeeId = payeeId;
    }

    public String getOldMobile() {
        return oldMobile;
    }

    public void setOldMobile(String oldMobile) {
        this.oldMobile = oldMobile;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getDevicePrint() {
        return devicePrint;
    }

    public void setDevicePrint(String devicePrint) {
        this.devicePrint = devicePrint;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToUserName() {
        return ToUserName;
    }

    public void setToUserName(String toUserName) {
        ToUserName = toUserName;
    }

    public String getToAccountNo() {
        return ToAccountNo;
    }

    public void setToAccountNo(String toAccountNo) {
        ToAccountNo = toAccountNo;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }
}
