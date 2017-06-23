package com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.model;

/**
 * ViewModel:修改收款人别名
 * Created by zhx on 2016/7/26
 */
public class PsnTransManagePayeeModifyAliasViewModel {
    /**
     * 收款人ID
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

    public String getPayeeId() {
        return payeeId;
    }

    public void setPayeeId(String payeeId) {
        this.payeeId = payeeId;
    }

    public String getOldAlias() {
        return oldAlias;
    }

    public void setOldAlias(String oldAlias) {
        this.oldAlias = oldAlias;
    }

    public String getPayeeAlias() {
        return payeeAlias;
    }

    public void setPayeeAlias(String payeeAlias) {
        this.payeeAlias = payeeAlias;
    }

    public String getDevicePrint() {
        return devicePrint;
    }

    public void setDevicePrint(String devicePrint) {
        this.devicePrint = devicePrint;
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
}
