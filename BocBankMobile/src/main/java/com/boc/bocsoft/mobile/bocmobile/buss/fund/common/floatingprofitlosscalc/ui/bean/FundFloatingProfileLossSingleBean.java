package com.boc.bocsoft.mobile.bocmobile.buss.fund.common.floatingprofitlosscalc.ui.bean;

/**
 * Created by taoyongzhen on 2016/12/3.
 */

public class FundFloatingProfileLossSingleBean {
    private int type;
    private String titleContent;
    private String valueContent;
    private String despContent;
    private boolean isShowCircle = true;
    private boolean isShowUpAxis = true;
    private boolean isShowDownAxis = true;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitleContent() {
        return titleContent;
    }

    public void setTitleContent(String titleContent) {
        this.titleContent = titleContent;
    }

    public String getValueContent() {
        return valueContent;
    }

    public void setValueContent(String valueContent) {
        this.valueContent = valueContent;
    }

    public String getDespContent() {
        return despContent;
    }

    public void setDespContent(String despContent) {
        this.despContent = despContent;
    }

    public boolean isShowDownAxis() {
        return isShowDownAxis;
    }

    public void setShowDownAxis(boolean showDownAxis) {
        isShowDownAxis = showDownAxis;
    }

    public boolean isShowUpAxis() {
        return isShowUpAxis;
    }

    public void setShowUpAxis(boolean showUpAxis) {
        isShowUpAxis = showUpAxis;
    }

    public boolean isShowCircle() {
        return isShowCircle;
    }

    public void setShowCircle(boolean showCircle) {
        isShowCircle = showCircle;
    }

}
