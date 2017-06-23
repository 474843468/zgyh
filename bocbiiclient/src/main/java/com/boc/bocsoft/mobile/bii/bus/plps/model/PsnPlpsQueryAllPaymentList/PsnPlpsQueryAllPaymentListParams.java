package com.boc.bocsoft.mobile.bii.bus.plps.model.PsnPlpsQueryAllPaymentList;

/**
 *  eyding 2016/08/01
 */
public class PsnPlpsQueryAllPaymentListParams {

  /**
   *   省的简称，如北京为BJ，上海为SH  M
   */
  private String  prvcShortName;

  /**
   * 城市多语言代码 M
   */
  private String cityDispNo;

    public void setCityDispNo(String cityDispNo) {
        this.cityDispNo = cityDispNo;
    }

    public void setPrvcShortName(String prvcShortName) {
        this.prvcShortName = prvcShortName;
    }

  public String getCityDispNo() {
    return cityDispNo;
  }

  public String getPrvcShortName() {
    return prvcShortName;
  }
}
