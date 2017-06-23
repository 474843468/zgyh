package com.boc.bocsoft.mobile.bocmobile.buss.login.model;

/**
 * 登录UI model
 * 面向前端UI界面，存储和提供前端使用的数据
 * Created by feibin on 2016/5/21.
 */
public class LoginViewModel {

    /**
     * 登录上送数据项
     */
    // 客户输入的登录名
    private String loginName;
    // 客户输入的密码
    private String password;
    // 客户输入的加密随机数
    private String password_RC;
    // 图形验证码
    private String validationChar;
    // 登录方式
    private String segment;
    // 登录标识
    private String wp7LoginType;
    private String activ;
    private String state;
    /**
     * 登录返回数据项
     */
    // 是否需要图形验证码
    private String needValidationChars;
    // 客户Id
    private String customerId;
    // 预留信息
    private String loginHint;
    // 客户姓名
    private String name;
    // 登录状态
    private String loginStatus;
    // 用户ID
    private String userId;
    // 证件号
    private String identityNumber;
    // 手机号
    private String mobile;
    // 注册类型 柜台注册：1 批量注册：3
    private String regtype;
    // 合并状态
    private String combinStatus;
    // 市场细分
    private String segmentId;

    /**客户等级信息*/
    private String customerLevel;

    public String getCustomerLevel() {
        return customerLevel;
    }

    public void setCustomerLevel(String customerLevel) {
        this.customerLevel = customerLevel;
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

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword_RC() {
        return password_RC;
    }

    public void setPassword_RC(String password_RC) {
        this.password_RC = password_RC;
    }

    public String getValidationChar() {
        return validationChar;
    }

    public void setValidationChar(String validationChar) {
        this.validationChar = validationChar;
    }

    public String getSegment() {
        return segment;
    }

    public void setSegment(String segment) {
        this.segment = segment;
    }

    public String getWp7LoginType() {
        return wp7LoginType;
    }

    public void setWp7LoginType(String wp7LoginType) {
        this.wp7LoginType = wp7LoginType;
    }

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