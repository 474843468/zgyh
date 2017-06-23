package com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessGetUpperLimitOfForeignCurr;

/**
 * 4.15 015PsnFessGetUpperLimitOfForeignCurr可结售汇金额上限试算
 * Created by wzn7074 on 2016/11/16.
 */
public class PsnFessGetUpperLimitOfForeignCurrResult {

    private String availableBalanceCUR;     //人民币可购外币最大金额    业务类型为01-结汇时为空
    private String annRmeAmtCUR;            //本年额度内剩余可/售结汇外币金额

    public void setAvailableBalanceCUR(String availableBalanceCUR) {
        this.availableBalanceCUR = availableBalanceCUR;
    }

    public void setAnnRmeAmtCUR(String annRmeAmtCUR) {
        this.annRmeAmtCUR = annRmeAmtCUR;
    }

    public String getAvailableBalanceCUR() {
        return availableBalanceCUR;
    }

    public String getAnnRmeAmtCUR() {
        return annRmeAmtCUR;
    }
}
