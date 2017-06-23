package com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.model;

/**
 * 批量主动收款提交
 * Created by zhx on 2016/8/13
 */
public class PsnBatchTransActCollectionSubmitViewModel {
    /**
     * 收款人账户ID
     */
    private String toAccountId;
    /**
     * 收款人姓名
     */
    private String payeeName;
    /**
     * 收款人账号
     */
    private String payeeActno;
    /**
     * 币种
     */
    private String currency;
    /**
     * 人均收款金额
     */
    private String notifyPayeeAmount;
    /**
     * 总金额
     */
    private String totalAmount;
    /**
     * 总笔数
     */
    private String totalNum;
    /**
     * 收款人手机号
     */
    private String payeeMobile;
    /**
     * 备注
     */
    private String remark;
    /**
     * 动态口令
     */
    private String Otp;
    /**
     * 防重机制，通过PSNGetTokenId接口获取
     */
    private String token;
    /**
     * CA认证生成的密文
     */
    private String _signedData;
    private String deviceInfo;
    private String Smc;
    private String devicePrint;
    private String Smc_RC;
    private String Otp_RC;
    private String deviceInfo_RC;
    private String state;
    private String activ;

    //======================================//
    // 下面大致对应接口响应的字段
    //======================================//

    /**
     * 批次号
     */
    private String batchSeq;
    /**
     * 交易状态
     */
    private String status;
    /**
     *
     */
    private String savePayeeStatus;

    public String getToAccountId() {
        return toAccountId;
    }

    public void setToAccountId(String toAccountId) {
        this.toAccountId = toAccountId;
    }

    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }

    public String getPayeeActno() {
        return payeeActno;
    }

    public void setPayeeActno(String payeeActno) {
        this.payeeActno = payeeActno;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getNotifyPayeeAmount() {
        return notifyPayeeAmount;
    }

    public void setNotifyPayeeAmount(String notifyPayeeAmount) {
        this.notifyPayeeAmount = notifyPayeeAmount;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(String totalNum) {
        this.totalNum = totalNum;
    }

    public String getPayeeMobile() {
        return payeeMobile;
    }

    public void setPayeeMobile(String payeeMobile) {
        this.payeeMobile = payeeMobile;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getOtp() {
        return Otp;
    }

    public void setOtp(String otp) {
        Otp = otp;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String get_signedData() {
        return _signedData;
    }

    public void set_signedData(String _signedData) {
        this._signedData = _signedData;
    }

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public String getSmc() {
        return Smc;
    }

    public void setSmc(String smc) {
        Smc = smc;
    }

    public String getDevicePrint() {
        return devicePrint;
    }

    public void setDevicePrint(String devicePrint) {
        this.devicePrint = devicePrint;
    }

    public String getSmc_RC() {
        return Smc_RC;
    }

    public void setSmc_RC(String smc_RC) {
        Smc_RC = smc_RC;
    }

    public String getOtp_RC() {
        return Otp_RC;
    }

    public void setOtp_RC(String otp_RC) {
        Otp_RC = otp_RC;
    }

    public String getDeviceInfo_RC() {
        return deviceInfo_RC;
    }

    public void setDeviceInfo_RC(String deviceInfo_RC) {
        this.deviceInfo_RC = deviceInfo_RC;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getActiv() {
        return activ;
    }

    public void setActiv(String activ) {
        this.activ = activ;
    }

    public String getBatchSeq() {
        return batchSeq;
    }

    public void setBatchSeq(String batchSeq) {
        this.batchSeq = batchSeq;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSavePayeeStatus() {
        return savePayeeStatus;
    }

    public void setSavePayeeStatus(String savePayeeStatus) {
        this.savePayeeStatus = savePayeeStatus;
    }
}