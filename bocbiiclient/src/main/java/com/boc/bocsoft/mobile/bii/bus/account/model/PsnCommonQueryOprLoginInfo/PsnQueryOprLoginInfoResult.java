package com.boc.bocsoft.mobile.bii.bus.account.model.PsnCommonQueryOprLoginInfo;

import java.io.Serializable;

/**
 * Created by huixiaobo on 2016/6/29.
 * 查询用户信息返回参数
 */
public class PsnQueryOprLoginInfoResult implements Serializable {

    /**登录名*/
    private String loginName;
    /**创建日期*/
    private String createDate;
    /**上次修改时间*/
    private String lastModify;
    /**客户的安全级别*/
    private Integer securityLevel;
    /**登录的安全级别*/
    private Integer loginSecurityLevel;
    /**登录时间*/
    private String loginDate;
    /**上次登录时间*/
    private String lastLogin;
    /**欢迎信息*/
    private String loginHint;
    /**登录状态*/
    private Integer loginStatus;
    /**性别*/
    private String gender;
    /**手机号码*/
    private String mobile;
    /**客户Id*/
    private Integer customerId;
    /**市场细分*/
    private Integer segmentId;
    /**客户种类*/
    private String customerType;
    /**客户姓名*/
    private String customerName;
    /**证件号码*/
    private String identityNumber;
    /**证件类型*/
    private String identityType;
    /**客户渠道*/
    private String channel;
    /**客户姓名*/
    private String status;
    /**核心系统客户号*/
    private String cifNumber;
    /**用户id*/
    private String operatorId;
    /**首选语言*/
    private String preferredLang;
    /**CA开通日期*/
    private String issueCADate;
    /**修改口令时间*/
    private String changePassDate;
    /**最后登录失败时间*/
    private String loginErrorDate;
    /**累计错误次数*/
    private String totalErrorTimes;
    /**当日错误次数*/
    private String dailyErrorTimes;
    /**登录IP地址*/
    private String loginIp;
    /**动态令牌厂商标识*/
    private String merchId;
    /**动态令牌id*/
    private String tokenId;
    /**Token日期*/
    private String issueTokenDate;
    /**Token过期日期*/
    private String tokenExpireDate;
    /**短信令牌厂商标识*/
    private String smcMerchId;
    /**短信令牌id*/
    private String smcTokenId;
    /**短信令牌开通日期*/
    private String smcTokenDate;
    /**网银收费模式*/
    private String feeMode;
    /**用户默认的安全组合ID*/
    private String defaultSafety;
    /**CA供应商标识*/
    private String caMerchId;
    /**CA证书ID*/
    private String certUid;
    /**CA证书到期时间*/
    private String certExpire;
    /**用户注册类型*/
    private String regtype;
    /**密钥类别*/
    private String keyAlg;
    /**密钥长度*/
    private String keyLength;
    /**是否绑定硬件*/
    private String hasBindingDevice;
    /**查询版客户类型*/
    private String qryCustType;

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public void setSecurityLevel(Integer securityLevel) {
        this.securityLevel = securityLevel;
    }

    public void setLastModify(String lastModify) {
        this.lastModify = lastModify;
    }

    public void setLoginSecurityLevel(Integer loginSecurityLevel) {
        this.loginSecurityLevel = loginSecurityLevel;
    }

    public void setLoginDate(String loginDate) {
        this.loginDate = loginDate;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    public void setLoginHint(String loginHint) {
        this.loginHint = loginHint;
    }

    public void setLoginStatus(Integer loginStatus) {
        this.loginStatus = loginStatus;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public void setSegmentId(Integer segmentId) {
        this.segmentId = segmentId;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setIdentityNumber(String identityNumber) {
        this.identityNumber = identityNumber;
    }

    public void setIdentityType(String identityType) {
        this.identityType = identityType;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCifNumber(String cifNumber) {
        this.cifNumber = cifNumber;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public void setPreferredLang(String preferredLang) {
        this.preferredLang = preferredLang;
    }

    public void setIssueCADate(String issueCADate) {
        this.issueCADate = issueCADate;
    }

    public void setChangePassDate(String changePassDate) {
        this.changePassDate = changePassDate;
    }

    public void setLoginErrorDate(String loginErrorDate) {
        this.loginErrorDate = loginErrorDate;
    }

    public void setTotalErrorTimes(String totalErrorTimes) {
        this.totalErrorTimes = totalErrorTimes;
    }

    public void setDailyErrorTimes(String dailyErrorTimes) {
        this.dailyErrorTimes = dailyErrorTimes;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public void setMerchId(String merchId) {
        this.merchId = merchId;
    }

    public void setIssueTokenDate(String issueTokenDate) {
        this.issueTokenDate = issueTokenDate;
    }

    public void setTokenExpireDate(String tokenExpireDate) {
        this.tokenExpireDate = tokenExpireDate;
    }

    public void setSmcMerchId(String smcMerchId) {
        this.smcMerchId = smcMerchId;
    }

    public void setSmcTokenId(String smcTokenId) {
        this.smcTokenId = smcTokenId;
    }

    public void setSmcTokenDate(String smcTokenDate) {
        this.smcTokenDate = smcTokenDate;
    }

    public void setFeeMode(String feeMode) {
        this.feeMode = feeMode;
    }

    public void setDefaultSafety(String defaultSafety) {
        this.defaultSafety = defaultSafety;
    }

    public void setCaMerchId(String caMerchId) {
        this.caMerchId = caMerchId;
    }

    public void setCertUid(String certUid) {
        this.certUid = certUid;
    }

    public void setCertExpire(String certExpire) {
        this.certExpire = certExpire;
    }

    public void setRegtype(String regtype) {
        this.regtype = regtype;
    }

    public void setKeyAlg(String keyAlg) {
        this.keyAlg = keyAlg;
    }

    public void setKeyLength(String keyLength) {
        this.keyLength = keyLength;
    }

    public void setHasBindingDevice(String hasBindingDevice) {
        this.hasBindingDevice = hasBindingDevice;
    }

    public void setQryCustType(String qryCustType) {
        this.qryCustType = qryCustType;
    }

    public String getFeeMode() {
        return feeMode;
    }

    public String getLoginName() {
        return loginName;
    }

    public String getCreateDate() {
        return createDate;
    }

    public String getLastModify() {
        return lastModify;
    }

    public Integer getSecurityLevel() {
        return securityLevel;
    }

    public Integer getLoginSecurityLevel() {
        return loginSecurityLevel;
    }

    public String getLoginDate() {
        return loginDate;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public String getLoginHint() {
        return loginHint;
    }

    public String getGender() {
        return gender;
    }

    public String getMobile() {
        return mobile;
    }

    public Integer getLoginStatus() {
        return loginStatus;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public Integer getSegmentId() {
        return segmentId;
    }

    public String getCustomerType() {
        return customerType;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getIdentityNumber() {
        return identityNumber;
    }

    public String getIdentityType() {
        return identityType;
    }

    public String getChannel() {
        return channel;
    }

    public String getStatus() {
        return status;
    }

    public String getCifNumber() {
        return cifNumber;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public String getPreferredLang() {
        return preferredLang;
    }

    public String getIssueCADate() {
        return issueCADate;
    }

    public String getChangePassDate() {
        return changePassDate;
    }

    public String getLoginErrorDate() {
        return loginErrorDate;
    }

    public String getTotalErrorTimes() {
        return totalErrorTimes;
    }

    public String getDailyErrorTimes() {
        return dailyErrorTimes;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public String getMerchId() {
        return merchId;
    }

    public String getTokenId() {
        return tokenId;
    }

    public String getIssueTokenDate() {
        return issueTokenDate;
    }

    public String getTokenExpireDate() {
        return tokenExpireDate;
    }

    public String getSmcMerchId() {
        return smcMerchId;
    }

    public String getSmcTokenId() {
        return smcTokenId;
    }

    public String getSmcTokenDate() {
        return smcTokenDate;
    }

    public String getDefaultSafety() {
        return defaultSafety;
    }

    public String getCaMerchId() {
        return caMerchId;
    }

    public String getCertUid() {
        return certUid;
    }

    public String getCertExpire() {
        return certExpire;
    }

    public String getRegtype() {
        return regtype;
    }

    public String getKeyAlg() {
        return keyAlg;
    }

    public String getKeyLength() {
        return keyLength;
    }

    public String getHasBindingDevice() {
        return hasBindingDevice;
    }

    public String getQryCustType() {
        return qryCustType;
    }

    @Override
    public String toString() {
        return "PsnQueryOprLoginInfoResult{" +
                "loginName='" + loginName + '\'' +
                ", createDate='" + createDate + '\'' +
                ", lastModify='" + lastModify + '\'' +
                ", securityLevel=" + securityLevel +
                ", loginSecurityLevel=" + loginSecurityLevel +
                ", loginDate='" + loginDate + '\'' +
                ", lastLogin='" + lastLogin + '\'' +
                ", loginHint='" + loginHint + '\'' +
                ", loginStatus=" + loginStatus +
                ", gender='" + gender + '\'' +
                ", mobile='" + mobile + '\'' +
                ", customerId=" + customerId +
                ", segmentId=" + segmentId +
                ", customerType='" + customerType + '\'' +
                ", customerName='" + customerName + '\'' +
                ", identityNumber='" + identityNumber + '\'' +
                ", identityType='" + identityType + '\'' +
                ", channel='" + channel + '\'' +
                ", status='" + status + '\'' +
                ", cifNumber='" + cifNumber + '\'' +
                ", operatorId='" + operatorId + '\'' +
                ", preferredLang='" + preferredLang + '\'' +
                ", issueCADate='" + issueCADate + '\'' +
                ", changePassDate='" + changePassDate + '\'' +
                ", loginErrorDate='" + loginErrorDate + '\'' +
                ", totalErrorTimes='" + totalErrorTimes + '\'' +
                ", dailyErrorTimes='" + dailyErrorTimes + '\'' +
                ", loginIp='" + loginIp + '\'' +
                ", merchId='" + merchId + '\'' +
                ", tokenId='" + tokenId + '\'' +
                ", issueTokenDate='" + issueTokenDate + '\'' +
                ", tokenExpireDate='" + tokenExpireDate + '\'' +
                ", smcMerchId='" + smcMerchId + '\'' +
                ", smcTokenId='" + smcTokenId + '\'' +
                ", smcTokenDate='" + smcTokenDate + '\'' +
                ", feeMode='" + feeMode + '\'' +
                ", defaultSafety='" + defaultSafety + '\'' +
                ", caMerchId='" + caMerchId + '\'' +
                ", certUid='" + certUid + '\'' +
                ", certExpire='" + certExpire + '\'' +
                ", regtype='" + regtype + '\'' +
                ", keyAlg='" + keyAlg + '\'' +
                ", keyLength='" + keyLength + '\'' +
                ", hasBindingDevice='" + hasBindingDevice + '\'' +
                ", qryCustType='" + qryCustType + '\'' +
                '}';
    }
}
