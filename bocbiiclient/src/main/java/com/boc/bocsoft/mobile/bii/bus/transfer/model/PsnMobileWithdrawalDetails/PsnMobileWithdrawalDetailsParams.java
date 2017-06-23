package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnMobileWithdrawalDetails;

/**
 * 汇款解付详细信息请求
 *
 * Created by liuweidong on 2016/8/4.
 */
public class PsnMobileWithdrawalDetailsParams {

    /**
     * 收款人姓名
     */
    private String payeeName;
    /**
     * 收款人手机号
     */
    private String payeeMobile;
    /**
     * 汇款编号
     */
    private String remitNo;

    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }

    public String getPayeeMobile() {
        return payeeMobile;
    }

    public void setPayeeMobile(String payeeMobile) {
        this.payeeMobile = payeeMobile;
    }

    public String getRemitNo() {
        return remitNo;
    }

    public void setRemitNo(String remitNo) {
        this.remitNo = remitNo;
    }
}
