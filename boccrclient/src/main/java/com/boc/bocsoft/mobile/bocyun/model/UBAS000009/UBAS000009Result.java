package com.boc.bocsoft.mobile.bocyun.model.UBAS000009;

import java.util.List;

/**
 * Created by dingeryue on 2016年10月16.
 * 查询客户云备份信息所需交易
 */

public class UBAS000009Result {

  private String bancsCustNo;
  private String totalOrderCnt;

  private List<YunItemBen> list;

  public String getBancsCustNo() {
    return bancsCustNo;
  }

  public String getTotalOrderCnt() {
    return totalOrderCnt;
  }

  public List<YunItemBen> getList() {
    return list;
  }

  public static class  YunItemBen{

    /**\
     *  10
     交易码
     该字段表示：获取客户全部云备份信息，需要后续执行哪些交易
     */
    private String trCode;

    public String getTrCode() {
      return trCode;
    }
  }
}
