package com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanRegisterSumbitInterfaces;

import java.math.BigDecimal;

/**
 * 贷款管理-中银E贷-申请贷款-额度签约申请提交交易请求参数
 * Created by xintong on 2016/6/16.
 */
public class PsnLoanRegisterSumbitInterfacesParams {

    //创建交易会话后的id
    private String conversationId;
    //手机交易码（可选值）
    private String Smc;
    //动态口令（可选值）
    private String Otp;
    //防重机制，通过PSNGetTokenId接口获取
    private String token;
    //CA认证生成的密文（可选值）
    private String _signedData;
    //贷款产品编号（固定值OC-LOAN）
    private String loanPrdNo;
    //客户姓名
    private String custName;
    //证件类型（01  身份证）
    private String cerType;
    //证件号（字母大写）
    private String cerNo;
    //国家行政区划代码
    private String zoneCode;
    //手机号
    private String mobile;
    //街道信息（填写街道信息）
    private String streetInfo;
    //联系地址（详细地址信息）
    private String linkAddress;
    //联系关系（01-父母 02-配偶 03-兄弟 04-姐妹）
    private String linkRelation;
    //联系姓名
    private String linkName;
    //联系电话
    private String linkMobile;
    //还款账户ID
    private String loanRepayAccountId;
    //还款账户（只支持借记卡 119）
    private String loanRepayAccount;
    //合同模板编号
    private String contractFormId;
    //预授信额度
    private BigDecimal applyQuotet;
    //币种（CNY）
    private String currencyCode;
    //三方协议编号（数据共享及征信查询协议）
    private String threeContractNo;
    //语言类型CHN-中文ENG-英文
    private String eLanguage;


    //新添字段  用于获取操作员ID
    private String deviceInfo;
    private String deviceInfo_RC;
    private String devicePrint;
    private String Smc_RC;
    private String Otp_RC;

    private String activ;
    private String state;

    public String getActiv() {
        return activ;
    }

    public void setActiv(String activ) {
        this.activ = activ;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public String getDeviceInfo_RC() {
        return deviceInfo_RC;
    }

    public void setDeviceInfo_RC(String deviceInfo_RC) {
        this.deviceInfo_RC = deviceInfo_RC;
    }

    public String getDevicePrint() {
        return devicePrint;
    }

    public void setDevicePrint(String devicePrint) {
        this.devicePrint = devicePrint;
    }

    public String getSmc() {
        return Smc;
    }

    public void setSmc(String smc) {
        Smc = smc;
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

    public String getLoanPrdNo() {
        return loanPrdNo;
    }

    public void setLoanPrdNo(String loanPrdNo) {
        this.loanPrdNo = loanPrdNo;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getCerType() {
        return cerType;
    }

    public void setCerType(String cerType) {
        this.cerType = cerType;
    }

    public String getCerNo() {
        return cerNo;
    }

    public void setCerNo(String cerNo) {
        this.cerNo = cerNo;
    }

    public String getZoneCode() {
        return zoneCode;
    }

    public void setZoneCode(String zoneCode) {
        this.zoneCode = zoneCode;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }



    public String getLinkAddress() {
        return linkAddress;
    }

    public void setLinkAddress(String linkAddress) {
        this.linkAddress = linkAddress;
    }

    public String getLinkRelation() {
        return linkRelation;
    }

    public void setLinkRelation(String linkRelation) {
        this.linkRelation = linkRelation;
    }

    public String getLinkName() {
        return linkName;
    }

    public void setLinkName(String linkName) {
        this.linkName = linkName;
    }

    public String getLinkMobile() {
        return linkMobile;
    }

    public void setLinkMobile(String linkMobile) {
        this.linkMobile = linkMobile;
    }

    public String getLoanRepayAccountId() {
        return loanRepayAccountId;
    }

    public void setLoanRepayAccountId(String loanRepayAccountId) {
        this.loanRepayAccountId = loanRepayAccountId;
    }

    public String getLoanRepayAccount() {
        return loanRepayAccount;
    }

    public void setLoanRepayAccount(String loanRepayAccount) {
        this.loanRepayAccount = loanRepayAccount;
    }

    public String getContractFormId() {
        return contractFormId;
    }

    public void setContractFormId(String contractFormId) {
        this.contractFormId = contractFormId;
    }

    public BigDecimal getApplyQuotet() {
        return applyQuotet;
    }

    public void setApplyQuotet(BigDecimal applyQuotet) {
        this.applyQuotet = applyQuotet;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getStreetInfo() {
        return streetInfo;
    }

    public void setStreetInfo(String streetInfo) {
        this.streetInfo = streetInfo;
    }

    public String getThreeContractNo() {
        return threeContractNo;
    }

    public void setThreeContractNo(String threeContractNo) {
        this.threeContractNo = threeContractNo;
    }

    public String geteLanguage() {
        return eLanguage;
    }

    public void seteLanguage(String eLanguage) {
        this.eLanguage = eLanguage;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    @Override
    public String toString() {
        return "PsnLoanRegisterSumbitInterfacesParams{" +
                "conversationId='" + conversationId + '\'' +
                ", Smc='" + Smc + '\'' +
                ", Otp='" + Otp + '\'' +
                ", token='" + token + '\'' +
                ", _signedData='" + _signedData + '\'' +
                ", loanPrdNo='" + loanPrdNo + '\'' +
                ", custName='" + custName + '\'' +
                ", cerType='" + cerType + '\'' +
                ", cerNo='" + cerNo + '\'' +
                ", zoneCode='" + zoneCode + '\'' +
                ", mobile='" + mobile + '\'' +
                ", streetInfo='" + streetInfo + '\'' +
                ", linkAddress='" + linkAddress + '\'' +
                ", linkRelation='" + linkRelation + '\'' +
                ", linkName='" + linkName + '\'' +
                ", linkMobile='" + linkMobile + '\'' +
                ", loanRepayAccountId='" + loanRepayAccountId + '\'' +
                ", loanRepayAccount='" + loanRepayAccount + '\'' +
                ", contractFormId='" + contractFormId + '\'' +
                ", applyQuotet=" + applyQuotet +
                ", currencyCode='" + currencyCode + '\'' +
                ", threeContractNo='" + threeContractNo + '\'' +
                ", eLanguage='" + eLanguage + '\'' +
                '}';
    }
}
