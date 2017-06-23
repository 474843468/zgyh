package com.boc.bocsoft.mobile.bocmobile.buss.system.life.event;

import com.boc.bocsoft.mobile.bocmobile.buss.system.life.model.LifeMenuModel;

/**
 * Created by dingeryue on 2016年11月10.
 * 有新的用户常用被添加事件
 */

public class LifeCommUseNewEvent {

  private LifeMenuModel data;
  public LifeCommUseNewEvent(LifeMenuModel data){
    this.data = data;
  }

  public LifeMenuModel getData() {
    return data;
  }
}
