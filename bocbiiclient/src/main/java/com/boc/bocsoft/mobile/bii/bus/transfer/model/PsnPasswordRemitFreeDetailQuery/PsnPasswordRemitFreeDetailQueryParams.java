package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnPasswordRemitFreeDetailQuery;

/**
 * 密码汇款详情查询（包括收款，取消汇款和交易详情查询）
 * ATM无卡取款查询详情请求
 *
 * Created by liuweidong on 2016/6/25.
 */
public class PsnPasswordRemitFreeDetailQueryParams {
    /**
     * 汇款编号 必输，9~13位数字
     */
    private String remitNo;
    /**
     * 汇款人姓名
     */
    private String payerName;
    /**
     * 收款人姓名
     */
    private String payeeName;
    /**
     * 交易类型 必输，CL-取消汇款、OK-收款、QU-查询
     */
    private String freeRemitTrsType;

    public String getRemitNo() {
        return remitNo;
    }

    public void setRemitNo(String remitNo) {
        this.remitNo = remitNo;
    }

    public String getPayerName() {
        return payerName;
    }

    public void setPayerName(String payerName) {
        this.payerName = payerName;
    }

    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }

    public String getFreeRemitTrsType() {
        return freeRemitTrsType;
    }

    public void setFreeRemitTrsType(String freeRemitTrsType) {
        this.freeRemitTrsType = freeRemitTrsType;
    }
}
