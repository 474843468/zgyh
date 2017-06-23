package com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdCheckPaymentInfo;

/**
 * 作者：xwg on 16/11/21 17:13
 */
public class PsnCrcdCheckPaymentInfoResult {
    /**
    *   卡片是否过期标识		“1”：卡片过期
     “0”:卡片没过期
     */
    private String crcdOverdueFlag;
    /**
    *   卡失效日期 YYYY-MM-DD
    */
    private String crcdInvalidDate;
    /**
     * 卡片状态		“ ” 正常
     “ACTP”– 待激活
     …… 详见附录卡状态表
     */
    private String crcdState;


    public String getCrcdInvalidDate() {
        return crcdInvalidDate;
    }

    public void setCrcdInvalidDate(String crcdInvalidDate) {
        this.crcdInvalidDate = crcdInvalidDate;
    }

    public String getCrcdOverdueFlag() {
        return crcdOverdueFlag;
    }

    public void setCrcdOverdueFlag(String crcdOverdueFlag) {
        this.crcdOverdueFlag = crcdOverdueFlag;
    }

    public String getCrcdState() {
        return crcdState;
    }

    public void setCrcdState(String crcdState) {
        this.crcdState = crcdState;
    }
}
