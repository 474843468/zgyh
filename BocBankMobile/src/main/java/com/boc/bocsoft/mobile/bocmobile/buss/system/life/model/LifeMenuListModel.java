package com.boc.bocsoft.mobile.bocmobile.buss.system.life.model;

import java.util.List;

/**
 * Created by dingeryue on 2016年10月12.
 */

public class LifeMenuListModel {
  private List<LifeMenuModel> list;
  private LifeVo.CityVo cityVo;

  public void setList(List<LifeMenuModel> list) {
    this.list = list;
  }

  public void setCityVo(LifeVo.CityVo cityVo) {
    this.cityVo = cityVo;
  }

  public LifeVo.CityVo getCityVo() {
    return cityVo;
  }

  public List<LifeMenuModel> getList() {
    return list;
  }
}
