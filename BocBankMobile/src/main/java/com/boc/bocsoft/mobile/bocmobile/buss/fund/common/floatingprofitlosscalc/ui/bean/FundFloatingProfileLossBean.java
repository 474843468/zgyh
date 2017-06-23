package com.boc.bocsoft.mobile.bocmobile.buss.fund.common.floatingprofitlosscalc.ui.bean;

/**
 * Created by taoyongzhen on 2016/12/1.
 */

public class FundFloatingProfileLossBean {
    private String leftContent;// 基金名称(基金代码)
    private String rightFirstContent; //基金数值
    private String rightSecondContent;// 基金单位

    public String getRightSecondContent() {
        return rightSecondContent;
    }

    public void setRightSecondContent(String rightSecondContent) {
        this.rightSecondContent = rightSecondContent;
    }

    public String getLeftContent() {
        return leftContent;
    }

    public void setLeftContent(String leftContent) {
        this.leftContent = leftContent;
    }

    public String getRightFirstContent() {
        return rightFirstContent;
    }

    public void setRightFirstContent(String rightFirstContent) {
        this.rightFirstContent = rightFirstContent;
    }
}
