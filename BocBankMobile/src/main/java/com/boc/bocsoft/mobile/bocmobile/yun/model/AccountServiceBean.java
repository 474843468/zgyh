package com.boc.bocsoft.mobile.bocmobile.yun.model;

/**
 * 账号id 交易类型对照表
 * Created by dingeryue on 2016年10月16.
 */

public class AccountServiceBean {

  private String serviceCode;//交易类型编码
  private String lastAccId;//账户ID
  private String state;

  /**
   * 网银客户id
   */
  private String bocnetCustNo;

  /**
   * 核心系统客户号
   */
  private String bancsCustNo;

  /**
   * 客户注册手机号
   */
  private String mobilePh;


  public String getLastAccId() {
    return lastAccId;
  }

  public String getServiceCode() {
    return serviceCode;
  }

  public void setServiceCode(String serviceCode) {
    this.serviceCode = serviceCode;
  }

  public void setLastAccId(String lastAccId) {
    this.lastAccId = lastAccId;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getState() {
    return state;
  }

  public String getBocnetCustNo() {
    return bocnetCustNo;
  }

  public void setBocnetCustNo(String bocnetCustNo) {
    this.bocnetCustNo = bocnetCustNo;
  }

  public String getBancsCustNo() {
    return bancsCustNo;
  }

  public void setBancsCustNo(String bancsCustNo) {
    this.bancsCustNo = bancsCustNo;
  }

  public String getMobilePh() {
    return mobilePh;
  }

  public void setMobilePh(String mobilePh) {
    this.mobilePh = mobilePh;
  }
}
