package com.boc.bocsoft.mobile.bii.bus.plps.model.PsnPlpsQueryAllPaymentList;

import java.util.List;

/**
 * 查询某地区所有缴费项目 - result
 * Created by eyding on 2016/08/01.
 */
public class PsnPlpsQueryAllPaymentListResult {

  private List<PaymentBean> allPaymentList;

  public List<PaymentBean> getAllPaymentList() {
    return allPaymentList;
  }

  public class PaymentBean{

    /**
     * 菜单id
     */
    private String menuId;
    /**
     * 一个缴费项目唯一ID
     */
    private String catId;
    /**
     * catName
     */
    private String catName;
    /**
     * 是否可缴费  1：是 0：否（置灰）
     */
    private String isAvailable;

    public String getCatId() {
      return catId;
    }

    public String getCatName() {
      return catName;
    }

    public String getIsAvailable() {
      return isAvailable;
    }

    public String getMenuId() {
      return menuId;
    }
  }

}

