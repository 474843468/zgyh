package com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayGetQRCode;

/**
 * 获取二维码
 * Created by wangf on 2016/8/29.
 */
public class QRPayGetQRCodeResult {

    private String seqNo;//二维码流水号
    private String lifeTime;//支付串的有效期
    private String getConfirmInfoFreq;//前端刷新频率

    public String getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(String seqNo) {
        this.seqNo = seqNo;
    }

    public String getLifeTime() {
        return lifeTime;
    }

    public void setLifeTime(String lifeTime) {
        this.lifeTime = lifeTime;
    }

    public String getGetConfirmInfoFreq() {
        return getConfirmInfoFreq;
    }

    public void setGetConfirmInfoFreq(String getConfirmInfoFreq) {
        this.getConfirmInfoFreq = getConfirmInfoFreq;
    }
}
