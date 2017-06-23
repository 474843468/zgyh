package com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayGetConfirmInfo;

/**
 *查询反扫后的交易确认通知
 * Created by wangf on 2016/8/30.
 */
public class QRPayGetConfirmInfoResult {

    //商户名称
    private String merchantName;
    //商户号
    private String merchantNo;
    //商户类别代码
    private String merchantType;
    //支付金额
    private String amount;
    //系统跟踪号 -- ICCD返回的跟踪号
    private String settleKey;
    private String resStatus;

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

    public String getMerchantType() {
        return merchantType;
    }

    public void setMerchantType(String merchantType) {
        this.merchantType = merchantType;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getSettleKey() {
        return settleKey;
    }

    public void setSettleKey(String settleKey) {
        this.settleKey = settleKey;
    }

    public String getResStatus() {
        return resStatus;
    }

    public void setResStatus(String resStatus) {
        this.resStatus = resStatus;
    }
}
