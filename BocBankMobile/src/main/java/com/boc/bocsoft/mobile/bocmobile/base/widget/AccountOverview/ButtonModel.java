package com.boc.bocsoft.mobile.bocmobile.base.widget.AccountOverview;

/**
 * 借记卡余额详情下面连续按钮组件bean
 * Created by niuguobin on 2016/6/16.
 */
public class ButtonModel {
    private String name;
    private int imgName;
    private int id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImgName() {
        return imgName;
    }

    public void setImgName(int imgName) {
        this.imgName = imgName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ButtonModel(int id, String name, int imgName) {
        this.id = id;
        this.name = name;
        this.imgName = imgName;
    }

    public ButtonModel(int id,String name) {
        this.name = name;
        this.id = id;
    }

    public ButtonModel() {
    }
}
