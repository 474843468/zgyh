package com.boc.bocsoft.mobile.bocyun.model.UBAS000005;

/**
 * Created by dingeryue on 2016年10月16.
 * UBAS000005	 查询客户上一次交易使用的账户ID
 */

public class UserLastAccountIdGetParam {
  private String bancsCustNo	;//String	16	核心客户号
  private String serviceNo	;//String	16	交易类型编码	为空时，该查询接口返回该客户所有信息
  private String orderCnt		;//Integer	4	查询笔数	取值：50
  private String curStrNo		;//Integer	4	本包起始笔数	第一包送1，下翻页送前一包返回报文中的本包起始笔数+查询笔数，上翻页送前一包返回报文中的本包起始笔数-查询笔数。

  public void setBancsCustNo(String bancsCustNo) {
    this.bancsCustNo = bancsCustNo;
  }

  public void setCurStrNo(String curStrNo) {
    this.curStrNo = curStrNo;
  }

  public void setOrderCnt(String orderCnt) {
    this.orderCnt = orderCnt;
  }

  public void setServiceNo(String serviceCode) {
    this.serviceNo = serviceCode;
  }
}
