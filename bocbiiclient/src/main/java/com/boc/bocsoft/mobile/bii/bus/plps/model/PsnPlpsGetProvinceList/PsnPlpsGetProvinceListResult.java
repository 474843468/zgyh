package com.boc.bocsoft.mobile.bii.bus.plps.model.PsnPlpsGetProvinceList;

import java.util.List;

/**
 * Created by eyding on 2016/08/02.
 */
public class PsnPlpsGetProvinceListResult {

  private List<ProvinceBean> provinceList;

  public List<ProvinceBean> getProvinceList() {
    return provinceList;
  }

  public void setProvinceList(List<ProvinceBean> provinceList) {
    this.provinceList = provinceList;
  }

  public static class ProvinceBean{

    /**
     * 省简称
     */
    private String prvcShortName;
    /**
     * 省显示名称
     */
    private String prvcName;

    public String getPrvcName() {
      return prvcName;
    }

    public String getPrvcShortName() {
      return prvcShortName;
    }

    @Override public String toString() {
      return "ProvinceBean{" +
          "prvcShortName='" + prvcShortName + '\'' +
          ", prvcName='" + prvcName + '\'' +
          '}';
    }
  }

  @Override public String toString() {
    return "PsnPlpsGetProvinceListResult{" +
        "provinceList=" + provinceList +
        '}';
  }
}

