package com.boc.bocsoft.mobile.bocmobile.base.widget.transactionlist;

import org.threeten.bp.LocalDate;

/**
 * 查询列表公共组件数据
 * Created by liuweidong on 2016/6/2.
 */
public class TransactionBean {
    private int titleID;// 设置需要的样式
    private LocalDate time;// 时间（年月日）
    private boolean isChangeColor;
    private String contentLeftAbove;// 左上内容
    private String contentLeftBelow;// 左下内容
    private String contentLeftBelowAgain;// 左下下内容
    private String contentRightAbove;
    private String contentRightBelow;// 右下内容

    public LocalDate getTime() {
        return time;
    }

    public void setTime(LocalDate time) {
        this.time = time;
    }

    public String getContentRightAbove() {
        return contentRightAbove;
    }

    public void setContentRightAbove(String contentRightAbove) {
        this.contentRightAbove = contentRightAbove;
    }

    public int getTitleID() {
        return titleID;
    }

    public void setTitleID(int titleID) {
        this.titleID = titleID;
    }

    public String getContentLeftAbove() {
        return contentLeftAbove;
    }

    public void setContentLeftAbove(String contentLeftAbove) {
        this.contentLeftAbove = contentLeftAbove;
    }

    public String getContentLeftBelow() {
        return contentLeftBelow;
    }

    public void setContentLeftBelow(String contentLeftBelow) {
        this.contentLeftBelow = contentLeftBelow;
    }

    public String getContentLeftBelowAgain() {
        return contentLeftBelowAgain;
    }

    public void setContentLeftBelowAgain(String contentLeftBelowAgain) {
        this.contentLeftBelowAgain = contentLeftBelowAgain;
    }

    public String getContentRightBelow() {
        return contentRightBelow;
    }

    public void setContentRightBelow(String contentRightBelow) {
        this.contentRightBelow = contentRightBelow;
    }

    public boolean isChangeColor() {
        return isChangeColor;
    }

    public void setChangeColor(boolean changeColor) {
        isChangeColor = changeColor;
    }
}
