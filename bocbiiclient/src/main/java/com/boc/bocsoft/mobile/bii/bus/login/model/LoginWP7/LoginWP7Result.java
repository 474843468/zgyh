package com.boc.bocsoft.mobile.bii.bus.login.model.LoginWP7;

import com.boc.bocsoft.mobile.bii.common.model.BIIResponse;

/**
 * Created by feibin on 2016/5/19.
 */
public class LoginWP7Result {

    /**
     * 是否需要图形验证码
     */
    private String needValidationChars;
    /**
     * 客户Id
     */
    private String customerId;
    /**
     * 预留信息
     */
    private String loginHint;
    /**
     * 客户姓名
     */
    private String name;
    /**
     *登录状态
     */
    private String loginStatus;
    /**
     *用户ID
     */
    private String userId;
    /**
     *证件号
     */
    private String identityNumber;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 注册类型
     */
    private String regtype;
    /**
     * 合并状态
     */
    private String combinStatus;
    /**
     * 市场细分
     */
    private String segmentId;

    public String getNeedValidationChars() {
        return needValidationChars;
    }

    public void setNeedValidationChars(String needValidationChars) {
        this.needValidationChars = needValidationChars;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getLoginHint() {
        return loginHint;
    }

    public void setLoginHint(String loginHint) {
        this.loginHint = loginHint;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(String loginStatus) {
        this.loginStatus = loginStatus;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getIdentityNumber() {
        return identityNumber;
    }

    public void setIdentityNumber(String identityNumber) {
        this.identityNumber = identityNumber;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
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

    public String getSegmentId() {
        return segmentId;
    }

    public void setSegmentId(String segmentId) {
        this.segmentId = segmentId;
    }
}
