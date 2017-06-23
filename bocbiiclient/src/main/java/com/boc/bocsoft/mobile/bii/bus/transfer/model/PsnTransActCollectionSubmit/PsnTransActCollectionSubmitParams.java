package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActCollectionSubmit;

/**
 * 主动收款提交
 * Created by zhx on 2016/6/29.
 */
public class PsnTransActCollectionSubmitParams {
    /**
     * 会话ID
     */
    private String conversationId;
    /**
     * 币种
     */
    private String currency;
    /**
     * 收款金额
     */
    private String notifyPayeeAmount;
    /**
     * 备注
     */
    private String remark;
    /**
     * 付款人客户号
     */
    private String payerCustId;
    /**
     * 收款人账ID
     */
    private String toAccountId;
    /**
     * 收款人账号
     */
    private String payeeActno;
    /**
     * 付款人手机
     */
    private String payerMobile;
    /**
     * 付款人姓名
     */
    private String payerName;
    /**
     * 付款人类型
     */
    private String payerChannel;
    /**
     * 收款人手机
     */
    private String payeeMobile;
    /**
     * 收款人姓名
     */
    private String payeeName;
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

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPayerCustId() {
        return payerCustId;
    }

    public void setPayerCustId(String payerCustId) {
        this.payerCustId = payerCustId;
    }

    public String getToAccountId() {
        return toAccountId;
    }

    public void setToAccountId(String toAccountId) {
        this.toAccountId = toAccountId;
    }

    public String getPayeeActno() {
        return payeeActno;
    }

    public void setPayeeActno(String payeeActno) {
        this.payeeActno = payeeActno;
    }

    public String getPayerMobile() {
        return payerMobile;
    }

    public void setPayerMobile(String payerMobile) {
        this.payerMobile = payerMobile;
    }

    public String getPayerName() {
        return payerName;
    }

    public void setPayerName(String payerName) {
        this.payerName = payerName;
    }

    public String getPayerChannel() {
        return payerChannel;
    }

    public void setPayerChannel(String payerChannel) {
        this.payerChannel = payerChannel;
    }

    public String getPayeeMobile() {
        return payeeMobile;
    }

    public void setPayeeMobile(String payeeMobile) {
        this.payeeMobile = payeeMobile;
    }

    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }

    public String getOtp() {
        return Otp;
    }

    public void setOtp(String Otp) {
        this.Otp = Otp;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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

    public String getActiv() {
        return activ;
    }

    public void setActiv(String activ) {
        this.activ = activ;
    }
}
