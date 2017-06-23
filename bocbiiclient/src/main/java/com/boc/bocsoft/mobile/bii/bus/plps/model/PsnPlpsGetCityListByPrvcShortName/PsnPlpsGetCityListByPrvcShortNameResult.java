package com.boc.bocsoft.mobile.bii.bus.plps.model.PsnPlpsGetCityListByPrvcShortName;

import java.util.List;

/**
 * Created by eyding on 2016/08/02.
 */
public class PsnPlpsGetCityListByPrvcShortNameResult {

  private List<CityBean> cityList;

  public List<CityBean> getCityList() {
    return cityList;
  }

  public class CityBean{

    /**
     * 城市显示名称
     */
    private String cityName;
    /**
     * 城市 多语言代码
     */
    private String displayNo;

    public String getCityName() {
      return cityName;
    }

    public String getDisplayNo() {
      return displayNo;
    }
  }

}

