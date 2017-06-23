package com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGGetRegCurrency;

/**
 * Created by zc7067 on 2016/12/15.
 * 双向宝——获得结算币种
 */
public class PsnVFGGetRegCurrencyParams {
    //双向宝类型
    private String vfgType;

    public void setVfgType(String vfgType) {
        this.vfgType = vfgType;
    }

    public String getVfgType() {
        return vfgType;
    }

    public PsnVFGGetRegCurrencyParams() {
    }

    public PsnVFGGetRegCurrencyParams(String vfgType) {
        this.vfgType = vfgType;
    }
}
