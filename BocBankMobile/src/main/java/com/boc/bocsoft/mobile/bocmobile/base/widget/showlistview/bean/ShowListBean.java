package com.boc.bocsoft.mobile.bocmobile.base.widget.showlistview.bean;

import org.threeten.bp.LocalDate;

/**
 * Created by liuweidong on 2016/10/10.
 */
public class ShowListBean {
    public int type;// 子条目对应的类型
    public static final int TYPE_COUNT = 2;
    public static final int GROUP = 0;
    public static final int CHILD = 1;
    /*下面数据是使用者需要进行封装*/
    private boolean isChangeColor;//（可选）
    private int titleID;// 设置需要的样式（必选）
    private String groupName;// MM月/yyyy（必选）
    private LocalDate time;// 后台返回的时间字段（必选）
    private String contentLeftAbove;// 左上
    private String contentLeftBelow;// 左下
    private String contentLeftBelowAgain;// 左下下
    private String contentRightAbove;// 右上
    private String contentRightBelow;// 右下

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

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
