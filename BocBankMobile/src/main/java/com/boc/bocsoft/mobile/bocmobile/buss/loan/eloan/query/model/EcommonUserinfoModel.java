package com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.model;

/**
 * Created by huixiaobo on 2016/6/29.
 * 用户查询信息UiModel
 */
public class EcommonUserinfoModel {
    /**证件号码*/
    private String identityNumber;
    /**证件类型*/
    private String identityType;
    /**手机号码*/
    private String mobile;

    public void setIdentityNumber(String identityNumber) {
        this.identityNumber = identityNumber;
    }

    public void setIdentityType(String identityType) {
        this.identityType = identityType;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getIdentityNumber() {
        return identityNumber;
    }

    public String getIdentityType() {
        return identityType;
    }

    public String getMobile() {
        return mobile;
    }

    @Override
    public String toString() {
        return "EcommonUserinfoModel{" +
                "identityNumber='" + identityNumber + '\'' +
                ", identityType='" + identityType + '\'' +
                ", mobile='" + mobile + '\'' +
                '}';
    }
}
