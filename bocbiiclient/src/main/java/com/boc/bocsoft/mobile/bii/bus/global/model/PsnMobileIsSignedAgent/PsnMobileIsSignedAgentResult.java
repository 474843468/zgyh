package com.boc.bocsoft.mobile.bii.bus.global.model.PsnMobileIsSignedAgent;

/**
 * Created by dingeryue on 2016年11月29.
 * I12 4.12 012代理点签约判断: PsnMobileIsSignedAgent
 */

public class PsnMobileIsSignedAgentResult {
  /**
   * 代理点签约标识
   * “true”：签约，
   * “false”:未签约，只有签约时才显示代理点取款和取款查询
   */
  private String flag;

  public String getFlag() {
    return flag;
  }
}
