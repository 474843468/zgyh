package com.boc.bocsoft.mobile.bocmobile.buss.system.invest.event;

import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.Item;
import java.util.List;

/**
 * Created by dingeryue on 2016年11月01.
 */

public class InvestMenuChangeEvent {

  private List<Item> newOrder;
  public InvestMenuChangeEvent(List<Item> newOrder) {
    this.newOrder = newOrder;
  }

  public List<Item> getNewOrder() {
    return newOrder;
  }
}
