package com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.model;

/**
 * 判断是否存在相同收款人
 * Created by zhx on 2016/7/22
 */
public class PsnTransIsSamePayeeViewModel {
    //======================================//
    // 下面是“判断是否存在相同收款人”的字段
    //======================================//
    /**
     * 收款人账号
     */
    private String payeeActno;
    /**
     * Cnaps号
     */
    private String cnapsCode;
    /**
     * 收款账号标识（0：国内跨行 1：中行内转账 3：二代支付）
     */
    private String bocFlag;

    public String getPayeeActno() {
        return payeeActno;
    }

    public void setPayeeActno(String payeeActno) {
        this.payeeActno = payeeActno;
    }

    public String getBocFlag() {
        return bocFlag;
    }

    public void setBocFlag(String bocFlag) {
        this.bocFlag = bocFlag;
    }

    public String getCnapsCode() {
        return cnapsCode;
    }

    public void setCnapsCode(String cnapsCode) {
        this.cnapsCode = cnapsCode;
    }

    //======================================//
    // 下面大致对应接口响应的字段
    //======================================//
    private Boolean flag;

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }
}
