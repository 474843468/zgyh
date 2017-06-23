package com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayDoPayment;

import org.threeten.bp.LocalDateTime;

/**
 * 正扫支付
 * Created by wangf on 2016/8/30.
 */
public class QRPayDoPaymentResult {

    //交易币种
    private String curCode;
    //错误码
    private String errCode;
    //错误信息
    private String errMsg;
    private String merchantCatNo;
    //收款商户名称
    private String merchantName;
    //收款商户号
    private String merchantNo;
    //HHAP交易流水号
    private String settleKey;
    //交易状态
    private String status;
    //交易金额
    private String tranAmount;
    //收款商户号
    private String voucherNo;
    //交易时间
    private LocalDateTime tranTime;
    //网银交易流水号
    private String tranSeq;

    public String getCurCode() {
        return curCode;
    }

    public void setCurCode(String curCode) {
        this.curCode = curCode;
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

    public String getMerchantCatNo() {
        return merchantCatNo;
    }

    public void setMerchantCatNo(String merchantCatNo) {
        this.merchantCatNo = merchantCatNo;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getSettleKey() {
        return settleKey;
    }

    public void setSettleKey(String settleKey) {
        this.settleKey = settleKey;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTranAmount() {
        return tranAmount;
    }

    public void setTranAmount(String tranAmount) {
        this.tranAmount = tranAmount;
    }

    public String getVoucherNo() {
        return voucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }

    public LocalDateTime getTranTime() {
        return tranTime;
    }

    public void setTranTime(LocalDateTime tranTime) {
        this.tranTime = tranTime;
    }

    public String getTranSeq() {
        return tranSeq;
    }

    public void setTranSeq(String tranSeq) {
        this.tranSeq = tranSeq;
    }
}
