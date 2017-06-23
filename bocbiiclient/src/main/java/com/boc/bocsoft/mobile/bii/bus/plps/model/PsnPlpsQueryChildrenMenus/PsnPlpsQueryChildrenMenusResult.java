package com.boc.bocsoft.mobile.bii.bus.plps.model.PsnPlpsQueryChildrenMenus;

public class PsnPlpsQueryChildrenMenusResult {


  public static class ChildrenMenuBean {
    private String menusId;//菜单ID
    private String menusName;//菜单名称String
    private String menusFlag;//菜单标识代码

    public String getMenusId() {
      return menusId;
    }

    public String getMenusName() {
      return menusName;
    }

    public String getMenusFlag() {
      return menusFlag;
    }
  }
}

