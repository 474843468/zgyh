package com.boc.bocsoft.mobile.bocmobile.base.model;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * 用户实例接口
 * Created by Administrator on 2016/5/17.
 */
public class User implements Parcelable{


    // 当今用户是否登录
    private boolean isLogin;
    private String combinStatus;
    private String b2eIp;
    private String name;
    private String userId;
    private String currentDeviceFlag;
    private String custLevel; //客户等级

    /**
     * 代理点签约标识
     * true:签约，false 未签约
     */
    private boolean isMobileIsSignedAgent;

    private String loginName;//  登录名	String
    private String createDate;//创建日期	String
    private String lastModify;//上次修改时间	String
    private String securityLevel;// 客户的安全级别	Integer
    private String loginSecurityLevel;//登录的安全级别	Integer
    private String loginDate;//  登录时间	String
    private String lastLogin;//  上次登录时间	String
    private String loginHint;//  欢迎信息	String
    private String loginStatus;//  登录状态	Integer	0：正常 1：柜台首次登录2：柜台重置密码
    private String gender;//性别	String
    private String mobile;//手机号码	String
    private String customerId;//客户Id	Integer
    private String segmentId;//  市场细分	Integer
    private String customerType;//客户种类	String
    private String customerName;//客户姓名	String
    private String identityNumber;//证件号码	String
    private String identityType;//证件类型	String
    private String channel;//  客户渠道	String
    private String status;//客户状态	String
    private String cifNumber;//  核心系统客户号	String
    private String operatorId;//用户id	String
    private String preferredLang;//  首选语言	String
    private String issueCADate;//  CA开通日期	String
    private String changePassDate;//修改口令时间	String
    private String loginErrorDate;//最后登录失败时间	String
    private String totalErrorTimes;//  累计错误次数	String
    private String dailyErrorTimes;//  当日错误次数	String
    private String loginIp;//  登录IP地址	String
    private String merchId;//  动态令牌厂商标识	String
    private String tokenId;//  动态令牌id	String
    private String issueTokenDate;//Token日期	String
    private String tokenExpireDate;//  Token过期日期	String
    private String smcMerchId;//短信令牌厂商标识	String
    private String smcTokenId;//短信令牌id	String
    private String smcTokenDate;//短信令牌开通日期	String
    private String feeMode;//  网银收费模式	String
    private String defaultSafety;//  用户默认的安全组合ID	String
    private String caMerchId;//  CA供应商标识	String
    private String certUid;//  CA证书ID	String
    private String certExpire;//CA证书到期时间	String
    private String regtype;//  用户注册类型	String	1=柜台注册2=自助注册3=批量签约
    private String keyAlg;//密钥类别	String
    private String keyLength;//  密钥长度	String
    private String hasBindingDevice;//是否绑定硬件	String	0没有绑定 1绑定
    private String qryCustType;//  查询版客户类型	String	1 查询版老客户 2 查询版新客户（电子卡签约）3 查询版新客户（非电子卡签约）

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }
    public String getRegtype() {
        return regtype;
    }

    public void setRegtype(String regtype) {
        this.regtype = regtype;
    }

    public String getCombinStatus() {
        return combinStatus;
    }

    public void setCombinStatus(String combinStatus) {
        this.combinStatus = combinStatus;
    }

    public String getCaMerchId() {
        return caMerchId;
    }

    public void setCaMerchId(String caMerchId) {
        this.caMerchId = caMerchId;
    }

    public String getCertUid() {
        return certUid;
    }

    public void setCertUid(String certUid) {
        this.certUid = certUid;
    }

    public String getDefaultSafety() {
        return defaultSafety;
    }

    public void setDefaultSafety(String defaultSafety) {
        this.defaultSafety = defaultSafety;
    }

    public String getSecurityLevel() {
        return securityLevel;
    }

    public void setSecurityLevel(String securityLevel) {
        this.securityLevel = securityLevel;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getIdentityNumber() {
        return identityNumber;
    }

    public void setIdentityNumber(String identityNumber) {
        this.identityNumber = identityNumber;
    }

    public String getIdentityType() {
        return identityType;
    }

    public void setIdentityType(String identityType) {
        this.identityType = identityType;
    }

    public String getSegmentId() {
        return segmentId;
    }

    public void setSegmentId(String segmentId) {
        this.segmentId = segmentId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getLastModify() {
        return lastModify;
    }

    public void setLastModify(String lastModify) {
        this.lastModify = lastModify;
    }

    public String getLoginSecurityLevel() {
        return loginSecurityLevel;
    }

    public void setLoginSecurityLevel(String loginSecurityLevel) {
        this.loginSecurityLevel = loginSecurityLevel;
    }

    public String getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(String loginDate) {
        this.loginDate = loginDate;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getLoginHint() {
        return loginHint;
    }

    public void setLoginHint(String loginHint) {
        this.loginHint = loginHint;
    }

    public String getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(String loginStatus) {
        this.loginStatus = loginStatus;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getCifNumber() {
        return cifNumber;
    }

    public void setCifNumber(String cifNumber) {
        this.cifNumber = cifNumber;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public String getPreferredLang() {
        return preferredLang;
    }

    public void setPreferredLang(String preferredLang) {
        this.preferredLang = preferredLang;
    }

    public String getIssueCADate() {
        return issueCADate;
    }

    public void setIssueCADate(String issueCADate) {
        this.issueCADate = issueCADate;
    }

    public String getChangePassDate() {
        return changePassDate;
    }

    public void setChangePassDate(String changePassDate) {
        this.changePassDate = changePassDate;
    }

    public String getLoginErrorDate() {
        return loginErrorDate;
    }

    public void setLoginErrorDate(String loginErrorDate) {
        this.loginErrorDate = loginErrorDate;
    }

    public String getTotalErrorTimes() {
        return totalErrorTimes;
    }

    public void setTotalErrorTimes(String totalErrorTimes) {
        this.totalErrorTimes = totalErrorTimes;
    }

    public String getDailyErrorTimes() {
        return dailyErrorTimes;
    }

    public void setDailyErrorTimes(String dailyErrorTimes) {
        this.dailyErrorTimes = dailyErrorTimes;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public String getMerchId() {
        return merchId;
    }

    public void setMerchId(String merchId) {
        this.merchId = merchId;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getIssueTokenDate() {
        return issueTokenDate;
    }

    public void setIssueTokenDate(String issueTokenDate) {
        this.issueTokenDate = issueTokenDate;
    }

    public String getTokenExpireDate() {
        return tokenExpireDate;
    }

    public void setTokenExpireDate(String tokenExpireDate) {
        this.tokenExpireDate = tokenExpireDate;
    }

    public String getSmcMerchId() {
        return smcMerchId;
    }

    public void setSmcMerchId(String smcMerchId) {
        this.smcMerchId = smcMerchId;
    }

    public String getSmcTokenId() {
        return smcTokenId;
    }

    public void setSmcTokenId(String smcTokenId) {
        this.smcTokenId = smcTokenId;
    }

    public String getSmcTokenDate() {
        return smcTokenDate;
    }

    public void setSmcTokenDate(String smcTokenDate) {
        this.smcTokenDate = smcTokenDate;
    }

    public String getB2eIp() {
        return b2eIp;
    }

    public void setB2eIp(String b2eIp) {
        this.b2eIp = b2eIp;
    }

    public String getFeeMode() {
        return feeMode;
    }

    public void setFeeMode(String feeMode) {
        this.feeMode = feeMode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCertExpire() {
        return certExpire;
    }

    public void setCertExpire(String certExpire) {
        this.certExpire = certExpire;
    }

    public String getKeyAlg() {
        return keyAlg;
    }

    public void setKeyAlg(String keyAlg) {
        this.keyAlg = keyAlg;
    }

    public String getKeyLength() {
        return keyLength;
    }

    public void setKeyLength(String keyLength) {
        this.keyLength = keyLength;
    }

    public String getHasBindingDevice() {
        return hasBindingDevice;
    }

    public void setHasBindingDevice(String hasBindingDevice) {
        this.hasBindingDevice = hasBindingDevice;
    }

    public String getCurrentDeviceFlag() {
        return currentDeviceFlag;
    }

    public void setCurrentDeviceFlag(String currentDeviceFlag) {
        this.currentDeviceFlag = currentDeviceFlag;
    }

    public String getQryCustType() {
        return qryCustType;
    }

    public void setQryCustType(String qryCustType) {
        this.qryCustType = qryCustType;
    }

    public String getCustLevel() {
        return custLevel;
    }

    public void setCustLevel(String custLevel) {
        this.custLevel = custLevel;
    }

    public boolean isMobileIsSignedAgent() {
        return isMobileIsSignedAgent;
    }

    public void setMobileIsSignedAgent(boolean mobileIsSignedAgent) {
        isMobileIsSignedAgent = mobileIsSignedAgent;
    }

    public User() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(isLogin ? (byte) 1 : (byte) 0);
        dest.writeByte(isMobileIsSignedAgent ? (byte) 1 : (byte) 0);
        dest.writeString(this.combinStatus);
        dest.writeString(this.b2eIp);
        dest.writeString(this.name);
        dest.writeString(this.userId);
        dest.writeString(this.currentDeviceFlag);
        dest.writeString(this.custLevel);
        dest.writeString(this.loginName);
        dest.writeString(this.createDate);
        dest.writeString(this.lastModify);
        dest.writeString(this.securityLevel);
        dest.writeString(this.loginSecurityLevel);
        dest.writeString(this.loginDate);
        dest.writeString(this.lastLogin);
        dest.writeString(this.loginHint);
        dest.writeString(this.loginStatus);
        dest.writeString(this.gender);
        dest.writeString(this.mobile);
        dest.writeString(this.customerId);
        dest.writeString(this.segmentId);
        dest.writeString(this.customerType);
        dest.writeString(this.customerName);
        dest.writeString(this.identityNumber);
        dest.writeString(this.identityType);
        dest.writeString(this.channel);
        dest.writeString(this.status);
        dest.writeString(this.cifNumber);
        dest.writeString(this.operatorId);
        dest.writeString(this.preferredLang);
        dest.writeString(this.issueCADate);
        dest.writeString(this.changePassDate);
        dest.writeString(this.loginErrorDate);
        dest.writeString(this.totalErrorTimes);
        dest.writeString(this.dailyErrorTimes);
        dest.writeString(this.loginIp);
        dest.writeString(this.merchId);
        dest.writeString(this.tokenId);
        dest.writeString(this.issueTokenDate);
        dest.writeString(this.tokenExpireDate);
        dest.writeString(this.smcMerchId);
        dest.writeString(this.smcTokenId);
        dest.writeString(this.smcTokenDate);
        dest.writeString(this.feeMode);
        dest.writeString(this.defaultSafety);
        dest.writeString(this.caMerchId);
        dest.writeString(this.certUid);
        dest.writeString(this.certExpire);
        dest.writeString(this.regtype);
        dest.writeString(this.keyAlg);
        dest.writeString(this.keyLength);
        dest.writeString(this.hasBindingDevice);
        dest.writeString(this.qryCustType);
    }

    protected User(Parcel in) {
        this.isLogin = in.readByte() != 0;
        this.isMobileIsSignedAgent = in.readByte() != 0;
        this.combinStatus = in.readString();
        this.b2eIp = in.readString();
        this.name = in.readString();
        this.userId = in.readString();
        this.currentDeviceFlag = in.readString();
        this.custLevel = in.readString();
        this.loginName = in.readString();
        this.createDate = in.readString();
        this.lastModify = in.readString();
        this.securityLevel = in.readString();
        this.loginSecurityLevel = in.readString();
        this.loginDate = in.readString();
        this.lastLogin = in.readString();
        this.loginHint = in.readString();
        this.loginStatus = in.readString();
        this.gender = in.readString();
        this.mobile = in.readString();
        this.customerId = in.readString();
        this.segmentId = in.readString();
        this.customerType = in.readString();
        this.customerName = in.readString();
        this.identityNumber = in.readString();
        this.identityType = in.readString();
        this.channel = in.readString();
        this.status = in.readString();
        this.cifNumber = in.readString();
        this.operatorId = in.readString();
        this.preferredLang = in.readString();
        this.issueCADate = in.readString();
        this.changePassDate = in.readString();
        this.loginErrorDate = in.readString();
        this.totalErrorTimes = in.readString();
        this.dailyErrorTimes = in.readString();
        this.loginIp = in.readString();
        this.merchId = in.readString();
        this.tokenId = in.readString();
        this.issueTokenDate = in.readString();
        this.tokenExpireDate = in.readString();
        this.smcMerchId = in.readString();
        this.smcTokenId = in.readString();
        this.smcTokenDate = in.readString();
        this.feeMode = in.readString();
        this.defaultSafety = in.readString();
        this.caMerchId = in.readString();
        this.certUid = in.readString();
        this.certExpire = in.readString();
        this.regtype = in.readString();
        this.keyAlg = in.readString();
        this.keyLength = in.readString();
        this.hasBindingDevice = in.readString();
        this.qryCustType = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
