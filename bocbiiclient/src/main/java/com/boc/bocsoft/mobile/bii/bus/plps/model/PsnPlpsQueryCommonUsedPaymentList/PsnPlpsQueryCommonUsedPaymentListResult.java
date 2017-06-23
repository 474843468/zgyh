package com.boc.bocsoft.mobile.bii.bus.plps.model.PsnPlpsQueryCommonUsedPaymentList;

import java.util.List;

/**
 * 查询某地区所有缴费项目 - result
 * Created by eyding on 2016/08/01.
 */
public class PsnPlpsQueryCommonUsedPaymentListResult {

  private List<CommonPaymentBean> paymentList;

  public List<CommonPaymentBean> getPaymentList() {
    return paymentList;
  }

  public static class CommonPaymentBean{

    private String menuId	      ;// 菜单ID	String	1
    private String catId	        ;// 一个缴费项目唯一ID	Integer
    private String prvcShortName ;//	省简称	String
    private String prvcDispName	;//省显示名称	String
    private String cityDispName	;//城市显示名称	String
    private String cityDispNo	  ;//市多语言代码	String
    private String merchantName	;//商户名称	String
    private String flowFileId	  ;//流程文件ID	String
    private String catName	      ;//小类名称	String
    private String allDayService	;//是否支持7*24小时服务	String	1-是，0-否
    private String otherProvPay	;//是否支持跨省卡缴费	String	1-是，0-否
    private String crcdPay	      ;//是否支持信用卡缴费	String	1-是，0-否
    private String startTime	    ;//服务开始时间	Sting
    private String endTime	      ;//服务结束时间	Sting
    private String isAvalid	    ;//缴费项目是否有效	String	1：是 0：否（不可继续缴费）
    private String displayFlag 	;//此常用缴费项目是否在此终端展现	String	0：不展现 1：展现  前端需要根据此标识判断是否展现此常用缴费项目，并且前端计算常用缴费项目的个数，如果个数等于8，则不显示添加常用缴费功能的“加号”

    public String getMenuId() {
      return menuId;
    }

    public String getCatId() {
      return catId;
    }

    public String getPrvcShortName() {
      return prvcShortName;
    }

    public String getPrvcDispName() {
      return prvcDispName;
    }

    public String getCityDispName() {
      return cityDispName;
    }

    public String getCityDispNo() {
      return cityDispNo;
    }

    public String getMerchantName() {
      return merchantName;
    }

    public String getFlowFileId() {
      return flowFileId;
    }

    public String getCatName() {
      return catName;
    }

    public String getAllDayService() {
      return allDayService;
    }

    public String getOtherProvPay() {
      return otherProvPay;
    }

    public String getCrcdPay() {
      return crcdPay;
    }

    public String getStartTime() {
      return startTime;
    }

    public String getEndTime() {
      return endTime;
    }

    public String getIsAvalid() {
      return isAvalid;
    }

    public String getDisplayFlag() {
      return displayFlag;
    }
  }

}

