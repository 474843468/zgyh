package com.boc.bocsoft.mobile.bocmobile.buss.system.life.model;

/**
 * Created by dingeryue on 2016年09月03.
 * 生活菜单顺序模型  以 用户+地区 维度保存
 */
public class LifeMenuSortModel {

  //按地区排序 & 切换地区 重新排序（常用+地区）

  private String user;//用户 取用户loginName
  private String cityCode;//城市

  private String menuId;//菜单id
  private int sortIndex;//

  public String getUser() {
    return user;
  }

  public void setUser(String user) {
    this.user = user;
  }

  public String getCityCode() {
    return cityCode;
  }

  public void setCityCode(String cityCode) {
    this.cityCode = cityCode;
  }

  public String getMenuId() {
    return menuId;
  }

  public void setMenuId(String menuId) {
    this.menuId = menuId;
  }

  public int getSortIndex() {
    return sortIndex;
  }

  public void setSortIndex(int sortIndex) {
    this.sortIndex = sortIndex;
  }
}
