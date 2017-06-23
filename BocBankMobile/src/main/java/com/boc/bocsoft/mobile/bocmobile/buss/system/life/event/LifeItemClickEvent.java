package com.boc.bocsoft.mobile.bocmobile.buss.system.life.event;

import com.boc.bocsoft.mobile.bocmobile.buss.system.life.model.LifeMenuModel;

/**
 * Created by dingeryue on 2016年11月01.
 */

public class LifeItemClickEvent {

  private int pos;
  private LifeMenuModel data;
  public LifeItemClickEvent(int pos, LifeMenuModel data) {
    this.pos = pos;
    this.data = data;
  }

  public LifeMenuModel getData() {
    return data;
  }
}
