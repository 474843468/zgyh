package com.boc.bocsoft.mobile.bocyun.model.UBAS000009;

import com.boc.bocsoft.mobile.bocyun.common.model.YunResponse;

/**
 * Created by dingeryue on 2016年10月16.
 * 查询客户云备份信息所需交易
 */

public class UBAS000009Respone extends YunResponse<UBAS000009Result>{

  private String bancsCustNo;//核心客户号
  private String totalOrderCnt;//总记录数

  public String getBancsCustNo() {
    return bancsCustNo;
  }

  public String getTotalOrderCnt() {
    return totalOrderCnt;
  }

  public static class TrCodeBean{
    private String trCode;

    public String getTrCode() {
      return trCode;
    }
  }

}
