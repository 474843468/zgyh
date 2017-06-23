package com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayIsPassSet;

/**
 * 查询是否设置支付密码
 * Created by wangf on 2016/8/30.
 */
public class QRPayIsPassSetResult {

    //客户是否设置支付密码的标志位  0：未设置 1：已设置
    private String flag;

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}
