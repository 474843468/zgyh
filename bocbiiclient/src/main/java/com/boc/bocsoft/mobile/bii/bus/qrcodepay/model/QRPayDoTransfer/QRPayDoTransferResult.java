package com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayDoTransfer;

/**
 * Created by fanbin on 16/10/8.
 */
public class QRPayDoTransferResult {
    //交易状态 交易结果  0：成功 1：失败   2：未明
    private String status;
    //付款单号 付款单号  付款单号规则：系统标识（5位字符）+交易日期（8位）+交易时间（6位）+流水号（6位）共计25位
    private String orderNo;
    //付款时间
    private String orderTime;
    //网银账户流水号
    private String actSeq;
    //转账金额
    private String tranAmount;
    //收款方卡号
    private String payeeAccNo;
    //收款方姓名
    private String payeeName;
    //付款方附言
    private String payerComments;
    //付款凭证号  交易成功时返回
    private String voucherNum;
    //错误码  交易失败的错误码   当status是1时有值，否则为空
    private String errCode;
    //错误信息  交易失败的错误信息   当status是1时有值，否则为空
    private String errMsg;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getActSeq() {
        return actSeq;
    }

    public void setActSeq(String actSeq) {
        this.actSeq = actSeq;
    }

    public String getTranAmount() {
        return tranAmount;
    }

    public void setTranAmount(String tranAmount) {
        this.tranAmount = tranAmount;
    }

    public String getPayeeAccNo() {
        return payeeAccNo;
    }

    public void setPayeeAccNo(String payeeAccNo) {
        this.payeeAccNo = payeeAccNo;
    }

    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }

    public String getPayerComments() {
        return payerComments;
    }

    public void setPayerComments(String payerComments) {
        this.payerComments = payerComments;
    }

    public String getVoucherNum() {
        return voucherNum;
    }

    public void setVoucherNum(String voucherNum) {
        this.voucherNum = voucherNum;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }
}
