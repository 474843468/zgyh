package com.boc.bocsoft.mobile.bocmobile.base.widget.singlemoreselect;

/**
 * 单选多选组件的子条目数据
 * Created by liuweidong on 2016/6/2.
 */
public class Content implements Comparable<Content>{
    private String name;// 分类内容
    private String contentNameID;// 分类内容ID
    private boolean isSelected;// 内容是否选中
    public static final String SELECTED = "1";
    public static final String NOT_SELECTED = "0";

    public boolean getSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContentNameID() {
        return contentNameID;
    }

    public void setContentNameID(String contentNameID) {
        this.contentNameID = contentNameID;
    }

    @Override
    public int compareTo(Content another) {
        int left = Integer.parseInt(another.getContentNameID());
        int right = Integer.parseInt(getContentNameID());
        return left > right ? -1 : 1;
    }
}
