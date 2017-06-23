package com.boc.bocsoft.mobile.bocyun.model.UBAS000005;

import java.util.List;

/**
 * Created by dingeryue on 2016年10月16.
 * UBAS000005	 查询客户上一次交易使用的账户ID
 */

public class UserLastAccountIdGetResult {

  private String bancsCustNo		;//String	16	核心客户号
  private int curStrNo			;//Integer	4	本包起始笔数
  private int curOrderCnt		;//Integer	4	本包交易记录数
  private int totalOrderCnt	;//Integer	4	总交易记录数

  private List<AccountBean> list;

  public String getBancsCustNo() {
    return bancsCustNo;
  }

  public List<AccountBean> getList() {
    return list;
  }

  public int getCurOrderCnt() {
    return curOrderCnt;
  }

  public int getCurStrNo() {
    return curStrNo;
  }

  public int getTotalOrderCnt() {
    return totalOrderCnt;
  }

}
