package com.boc.bocsoft.mobile.bocyun.model.UBAS000009;

/**
 * Created by dingeryue on 2016年10月16.
 * 查询客户云备份信息所需交易
 */

public class UBAS000009Param {
  private String bancsCustNo ;//String16核心客户号

  private String orderCnt;//
  private String curStrNo;//初始为1

  public void setBancsCustNo(String bancsCustNo) {
    this.bancsCustNo = bancsCustNo;
  }

  public void setCurStrNo(String curStrNo) {
    this.curStrNo = curStrNo;
  }

  public void setOrderCnt(String orderCnt) {
    this.orderCnt = orderCnt;
  }
}
