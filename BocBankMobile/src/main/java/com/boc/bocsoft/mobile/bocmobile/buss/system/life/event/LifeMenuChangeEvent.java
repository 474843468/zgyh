package com.boc.bocsoft.mobile.bocmobile.buss.system.life.event;

import com.boc.bocsoft.mobile.bocmobile.buss.system.life.model.LifeMenuModel;
import java.util.List;

/**
 * Created by dingeryue on 2016年11月01.
 */

public class LifeMenuChangeEvent {

  private List<LifeMenuModel> newOrder;
  public LifeMenuChangeEvent(List<LifeMenuModel> newOrder) {
    this.newOrder = newOrder;
  }

  public List<LifeMenuModel> getNewOrder() {
    return newOrder;
  }
}
