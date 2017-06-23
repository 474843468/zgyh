package com.boc.bocsoft.mobile.bocmobile.buss.system.life.event;

import com.boc.bocsoft.mobile.bocmobile.buss.system.life.model.LifeMenuModel;

/**
 * Created by dingeryue on 2016年11月10.
 */

public class LifeDelCommUseEvent {
  private LifeMenuModel data;

  public LifeDelCommUseEvent(LifeMenuModel data){
    this.data = data;
  }

  public LifeMenuModel getData() {
    return data;
  }
}
