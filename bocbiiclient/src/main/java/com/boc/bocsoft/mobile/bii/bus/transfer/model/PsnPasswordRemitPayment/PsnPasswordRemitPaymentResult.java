package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnPasswordRemitPayment;

/**
 * ATM无卡取款交易响应
 *
 * Created by liuweidong on 2016/7/19.
 */
public class PsnPasswordRemitPaymentResult {
    /**
     * 到期日期
     */
    private String dueDate;
    /**
     * 交易状态
     */
    private String status;
    /**
     * 汇款编号
     */
    private String remitNo;

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemitNo() {
        return remitNo;
    }

    public void setRemitNo(String remitNo) {
        this.remitNo = remitNo;
    }
}
