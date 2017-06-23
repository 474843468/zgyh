package com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdQueryCardholderAddress;

/**
 * 信用卡补卡寄送地址查询响应
 *
 * Created by liuweidong on 2016/6/13.
 */
public class PsnCrcdQueryCardholderAddressResult {

    /**
     * 邮寄地址
     */
    private String mailAddress;
    /**
     * 邮寄地址类型
     */
    private String mailAddressType;

    public String getMailAddress() {
        return mailAddress;
    }

    public void setMailAddress(String mailAddress) {
        this.mailAddress = mailAddress;
    }

    public String getMailAddressType() {
        return mailAddressType;
    }

    public void setMailAddressType(String mailAddressType) {
        this.mailAddressType = mailAddressType;
    }
}
