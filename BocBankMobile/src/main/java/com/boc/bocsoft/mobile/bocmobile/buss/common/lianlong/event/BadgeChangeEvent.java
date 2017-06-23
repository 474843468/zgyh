package com.boc.bocsoft.mobile.bocmobile.buss.common.lianlong.event;

/**
 * Created by dingeryue on 2016年11月25.
 * 有新的消息
 */

public class BadgeChangeEvent {

  private boolean isShow;
  private int count;
  public BadgeChangeEvent(boolean isShow,int count){
    this.isShow = isShow;
    this.count = count;
  }

  public int getCount() {
    return count;
  }

  public boolean isShow() {
    return isShow;
  }
}
