package com.boc.bocsoft.mobile.bocmobile.buss.system.mine.model;

/**
 * Created by dingeryue on 2016年08月29.
 * 频道页-我的，菜单项目
 */
public class MineMenuVo {

  private String titleNoLogin;
  private String titleLogin;
  private int iconRes;
  private boolean isNeedLogin;
  private String target;//fragment 或者activity??

  public MineMenuVo titleNoLogin(String title){
    this.titleNoLogin  = title;
    return this;
  }

  public MineMenuVo titleLogin(String titleLogin){
    this.titleLogin = titleLogin;
    return this;
  }

  public MineMenuVo icon(int res){
    this.iconRes = res;
    return  this;
  }

  public MineMenuVo needLogin(boolean is){
    this.isNeedLogin = is;
    return  this;
  }

  public MineMenuVo target(String target){
    this.target = target;
    return this;
  }

  public String titleNoLogin(){
    return  this.titleNoLogin;
  }

  public String titleLogin(){
    return this.titleLogin;
  }

  public int icon(){
    return this.iconRes;
  }

  public boolean needLogin(){
    return  this.isNeedLogin;
  }

  public String target(){
    return  this.target;
  }
}
