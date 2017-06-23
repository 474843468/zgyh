package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransManagePayeeModifyAlias;

/**
 * 修改收款人别名
 * Created by zhx on 2016/7/26
 */
public class PsnTransManagePayeeModifyAliasParams {

    /**
     * payeeAlias : testName
     * ToAccountNo : 45609876543215432
     * token : v8w2gstn
     * oldAlias : oldName
     * payeeId : 66
     * devicePrint : version=&amp;pm_fpua=mozilla/4.0
     * ToUserName : zyt
     */
    /**
     * 收款人ID(这里是String，如果是Integer会报一大堆的错，错错错！)
     */
    private String payeeId;
    /**
     * 收款人旧别名
     */
    private String oldAlias;
    /**
     * 收款人别名
     */
    private String payeeAlias;
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

    public void setPayeeAlias(String payeeAlias) {
        this.payeeAlias = payeeAlias;
    }

    public void setToAccountNo(String ToAccountNo) {
        this.ToAccountNo = ToAccountNo;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setOldAlias(String oldAlias) {
        this.oldAlias = oldAlias;
    }

    public void setPayeeId(String payeeId) {
        this.payeeId = payeeId;
    }

    public void setDevicePrint(String devicePrint) {
        this.devicePrint = devicePrint;
    }

    public void setToUserName(String ToUserName) {
        this.ToUserName = ToUserName;
    }

    public String getPayeeAlias() {
        return payeeAlias;
    }

    public String getToAccountNo() {
        return ToAccountNo;
    }

    public String getToken() {
        return token;
    }

    public String getOldAlias() {
        return oldAlias;
    }

    public String getPayeeId() {
        return payeeId;
    }

    public String getDevicePrint() {
        return devicePrint;
    }

    public String getToUserName() {
        return ToUserName;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }
}
