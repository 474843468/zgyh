package com.chinamworld.bocmbci.userwidget.investview;

/**
 * 3.0风格更多菜单数据结构
 * Created by yuht on 2016/11/17.
 */
public class MoreMenuItem{
    public String getMenuStr() {
        return menuStr;
    }

    public void setMenuStr(String menuStr) {
        this.menuStr = menuStr;
    }

    private String menuStr;

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    private String menuId;

    public MoreMenuItem(String menuStr,String menuId){
        this.menuStr = menuStr;
        this.menuId = menuId;
    }
}
