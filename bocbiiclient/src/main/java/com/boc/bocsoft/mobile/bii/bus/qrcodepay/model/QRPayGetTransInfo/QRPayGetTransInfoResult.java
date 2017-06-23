package com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayGetTransInfo;

/**
 * 查询反扫支付交易信息
 * Created by wangf on 2016/9/19.
 */
public class QRPayGetTransInfoResult {

    //交易时间
    private String tranTime;
    //交易结果  0：成功 1：失败 2：未明 3：银行处理中
    private String tranStatus;
    //错误码
    private String errCode;
    //错误信息
    private String errMsg;
    //交易金额
    private String tranAmount;
    //付款凭证号 交易成功时返回
    private String voucherNum;
    //交易说明
    private String tranRemark;


    public String getTranTime() {
        return tranTime;
    }

    public void setTranTime(String tranTime) {
        this.tranTime = tranTime;
    }

    public String getTranStatus() {
        return tranStatus;
    }

    public void setTranStatus(String tranStatus) {
        this.tranStatus = tranStatus;
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

    public String getTranAmount() {
        return tranAmount;
    }

    public void setTranAmount(String tranAmount) {
        this.tranAmount = tranAmount;
    }

    public String getTranRemark() {
        return tranRemark;
    }

    public void setTranRemark(String tranRemark) {
        this.tranRemark = tranRemark;
    }

    public String getVoucherNum() {
        return voucherNum;
    }

    public void setVoucherNum(String voucherNum) {
        this.voucherNum = voucherNum;
    }
}
